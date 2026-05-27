package com.pacifico.claims.application.service;

import com.pacifico.claims.domain.exception.BusinessException;
import com.pacifico.claims.domain.exception.ClaimNotFoundException;
import com.pacifico.claims.domain.exception.CustomerNotFoundException;
import com.pacifico.claims.domain.exception.PolicyNotFoundException;
import com.pacifico.claims.domain.model.Claim;
import com.pacifico.claims.domain.model.ClaimStatus;
import com.pacifico.claims.domain.model.ClaimType;
import com.pacifico.claims.domain.model.Customer;
import com.pacifico.claims.domain.model.Policy;
import com.pacifico.claims.domain.port.out.ClaimRepositoryPort;
import com.pacifico.claims.domain.port.out.CustomerRepositoryPort;
import com.pacifico.claims.domain.port.out.PolicyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepositoryPort claimRepository;

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private PolicyRepositoryPort policyRepository;

    private ClaimService claimService;

    private UUID customerId;
    private UUID policyId;
    private UUID claimId;
    private Customer customer;
    private Policy activePolicy;

    @BeforeEach
    void setUp() {
        claimService = new ClaimService(claimRepository, customerRepository, policyRepository);
        customerId = UUID.randomUUID();
        policyId = UUID.randomUUID();
        claimId = UUID.randomUUID();

        customer = Customer.builder()
                .id(customerId)
                .name("Test Customer")
                .email("test@customer.com")
                .build();

        activePolicy = Policy.builder()
                .id(policyId)
                .customerId(customerId)
                .policyNumber("POL-12345")
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().plusDays(10))
                .coverageAmount(BigDecimal.valueOf(1000.00))
                .build();
    }

    @Test
    void create_HappyPath() {
        Claim claimToCreate = Claim.builder()
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("Dental clean")
                .amount(BigDecimal.valueOf(150.00))
                .build();

        Claim savedClaim = Claim.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("Dental clean")
                .amount(BigDecimal.valueOf(150.00))
                .claimStatus(ClaimStatus.PENDING)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(policyRepository.findById(policyId)).thenReturn(Mono.just(activePolicy));
        when(claimRepository.save(any(Claim.class))).thenReturn(Mono.just(savedClaim));

        Mono<Claim> result = claimService.create(claimToCreate);

        StepVerifier.create(result)
                .expectNext(savedClaim)
                .verifyComplete();

        verify(claimRepository, times(1)).save(any(Claim.class));
    }

    @Test
    void create_CustomerNotFound() {
        Claim claim = Claim.builder()
                .customerId(customerId)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Mono.empty());

        Mono<Claim> result = claimService.create(claim);

        StepVerifier.create(result)
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(claimRepository, never()).save(any());
    }

    @Test
    void create_PolicyNotFound() {
        Claim claim = Claim.builder()
                .customerId(customerId)
                .policyId(policyId)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(policyRepository.findById(policyId)).thenReturn(Mono.empty());

        Mono<Claim> result = claimService.create(claim);

        StepVerifier.create(result)
                .expectError(PolicyNotFoundException.class)
                .verify();

        verify(claimRepository, never()).save(any());
    }

    @Test
    void create_PolicyDoesNotBelongToCustomer() {
        Claim claim = Claim.builder()
                .customerId(customerId)
                .policyId(policyId)
                .amount(BigDecimal.valueOf(100.00))
                .build();

        Policy foreignPolicy = Policy.builder()
                .id(policyId)
                .customerId(UUID.randomUUID()) // different customer
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(policyRepository.findById(policyId)).thenReturn(Mono.just(foreignPolicy));

        Mono<Claim> result = claimService.create(claim);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().contains("does not belong to the customer"))
                .verify();
    }

    @Test
    void create_PolicyExpired() {
        Claim claim = Claim.builder()
                .customerId(customerId)
                .policyId(policyId)
                .amount(BigDecimal.valueOf(100.00))
                .build();

        Policy expiredPolicy = Policy.builder()
                .id(policyId)
                .customerId(customerId)
                .startDate(LocalDateTime.now().minusDays(30))
                .endDate(LocalDateTime.now().minusDays(5)) // expired 5 days ago
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(policyRepository.findById(policyId)).thenReturn(Mono.just(expiredPolicy));

        Mono<Claim> result = claimService.create(claim);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().contains("not currently active"))
                .verify();
    }

    @Test
    void create_AmountExceedsCoverage() {
        Claim claim = Claim.builder()
                .customerId(customerId)
                .policyId(policyId)
                .amount(BigDecimal.valueOf(1200.00)) // coverage limit is 1000
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(policyRepository.findById(policyId)).thenReturn(Mono.just(activePolicy));

        Mono<Claim> result = claimService.create(claim);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().contains("exceeds the policy coverage amount"))
                .verify();
    }

    @Test
    void findById_HappyPath() {
        Claim claim = Claim.builder().id(claimId).build();
        when(claimRepository.findById(claimId)).thenReturn(Mono.just(claim));

        StepVerifier.create(claimService.findById(claimId))
                .expectNext(claim)
                .verifyComplete();
    }

    @Test
    void findById_NotFound() {
        when(claimRepository.findById(claimId)).thenReturn(Mono.empty());

        StepVerifier.create(claimService.findById(claimId))
                .verifyComplete();
    }

    @Test
    void findAll_HappyPath() {
        Claim c1 = Claim.builder().id(UUID.randomUUID()).build();
        Claim c2 = Claim.builder().id(UUID.randomUUID()).build();

        when(claimRepository.findAll()).thenReturn(Flux.just(c1, c2));

        StepVerifier.create(claimService.findAll())
                .expectNext(c1)
                .expectNext(c2)
                .verifyComplete();
    }

    @Test
    void update_HappyPath() {
        Claim existing = Claim.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("Old Desc")
                .amount(BigDecimal.valueOf(100.00))
                .claimStatus(ClaimStatus.PENDING)
                .build();

        Claim updated = Claim.builder()
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("New Desc")
                .amount(BigDecimal.valueOf(200.00))
                .claimStatus(ClaimStatus.APPROVED)
                .build();

        Claim expected = Claim.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("New Desc")
                .amount(BigDecimal.valueOf(200.00))
                .claimStatus(ClaimStatus.APPROVED)
                .build();

        when(claimRepository.findById(claimId)).thenReturn(Mono.just(existing));
        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(policyRepository.findById(policyId)).thenReturn(Mono.just(activePolicy));
        when(claimRepository.save(any(Claim.class))).thenReturn(Mono.just(expected));

        StepVerifier.create(claimService.update(claimId, updated))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void update_NotFound() {
        when(claimRepository.findById(claimId)).thenReturn(Mono.empty());

        StepVerifier.create(claimService.update(claimId, Claim.builder().build()))
                .expectError(ClaimNotFoundException.class)
                .verify();
    }

    @Test
    void delete_HappyPath() {
        Claim existing = Claim.builder().id(claimId).build();
        when(claimRepository.findById(claimId)).thenReturn(Mono.just(existing));
        when(claimRepository.deleteById(claimId)).thenReturn(Mono.empty());

        StepVerifier.create(claimService.delete(claimId))
                .verifyComplete();

        verify(claimRepository, times(1)).deleteById(claimId);
    }

    @Test
    void delete_NotFound() {
        when(claimRepository.findById(claimId)).thenReturn(Mono.empty());

        StepVerifier.create(claimService.delete(claimId))
                .expectError(ClaimNotFoundException.class)
                .verify();
    }
}
