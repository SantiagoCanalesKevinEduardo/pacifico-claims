package com.pacifico.claims.application.service;

import com.pacifico.claims.domain.model.Claim;
import com.pacifico.claims.domain.model.ClaimStatus;
import com.pacifico.claims.domain.port.in.CreateClaimUseCase;
import com.pacifico.claims.domain.port.in.FindClaimUseCase;
import com.pacifico.claims.domain.port.out.ClaimRepositoryPort;
import com.pacifico.claims.domain.port.out.CustomerRepositoryPort;
import com.pacifico.claims.domain.port.out.PolicyRepositoryPort;
import com.pacifico.claims.domain.exception.BusinessException;
import com.pacifico.claims.domain.exception.CustomerNotFoundException;
import com.pacifico.claims.domain.exception.PolicyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClaimService implements CreateClaimUseCase, FindClaimUseCase {

    private final ClaimRepositoryPort claimRepository;
    private final CustomerRepositoryPort customerRepository;
    private final PolicyRepositoryPort policyRepository;

    @Override
    @Transactional
    public Mono<Claim> create(Claim claim) {
        return customerRepository.findById(claim.customerId())
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(claim.customerId())))
                .then(policyRepository.findById(claim.policyId()))
                .switchIfEmpty(Mono.error(new PolicyNotFoundException(claim.policyId())))
                .flatMap(policy -> {
                    // 1. Validar que la póliza pertenezca al cliente
                    if (!policy.customerId().equals(claim.customerId())) {
                        return Mono.error(new BusinessException("The policy with ID " + claim.policyId()
                                + " does not belong to the customer with ID " + claim.customerId()));
                    }

                    // 2. Validar vigencia de la póliza
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isBefore(policy.startDate()) || now.isAfter(policy.endDate())) {
                        return Mono.error(new BusinessException("The policy is not currently active. Active period: "
                                + policy.startDate() + " to " + policy.endDate()));
                    }

                    // 3. Validar límite de cobertura
                    if (claim.amount().compareTo(policy.coverageAmount()) > 0) {
                        return Mono.error(new BusinessException("Claim amount " + claim.amount()
                                + " exceeds the policy coverage amount of " + policy.coverageAmount()));
                    }

                    // Si pasa las validaciones, creamos el siniestro en estado PENDING
                    Claim claimToSave = Claim.builder()
                            .customerId(claim.customerId())
                            .policyId(claim.policyId())
                            .claimType(claim.claimType())
                            .description(claim.description())
                            .amount(claim.amount())
                            .claimStatus(ClaimStatus.PENDING)
                            .build();

                    return claimRepository.save(claimToSave);
                });
    }

    @Override
    public Mono<Claim> findById(UUID id) {
        return claimRepository.findById(id);
    }

    @Override
    public Flux<Claim> findAll() {
        return claimRepository.findAll();
    }
}
