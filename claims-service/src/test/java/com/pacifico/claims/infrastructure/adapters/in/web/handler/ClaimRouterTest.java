package com.pacifico.claims.infrastructure.adapters.in.web.handler;

import com.pacifico.claims.domain.exception.ClaimNotFoundException;
import com.pacifico.claims.domain.model.Claim;
import com.pacifico.claims.domain.model.ClaimStatus;
import com.pacifico.claims.domain.model.ClaimType;
import com.pacifico.claims.domain.port.in.CreateClaimUseCase;
import com.pacifico.claims.domain.port.in.DeleteClaimUseCase;
import com.pacifico.claims.domain.port.in.FindClaimUseCase;
import com.pacifico.claims.domain.port.in.UpdateClaimUseCase;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimResponse;
import com.pacifico.claims.infrastructure.adapters.in.web.mapper.ClaimWebMapper;
import com.pacifico.claims.infrastructure.security.JwtService;
import com.pacifico.claims.infrastructure.security.JwtAuthenticationFilter;
import com.pacifico.claims.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest
@Import({ClaimRouter.class, ClaimHandler.class, SecurityConfig.class, JwtAuthenticationFilter.class})
class ClaimRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.ReactiveUserDetailsService userDetailsService;

    @MockitoBean
    private CreateClaimUseCase createClaimUseCase;

    @MockitoBean
    private FindClaimUseCase findClaimUseCase;

    @MockitoBean
    private UpdateClaimUseCase updateClaimUseCase;

    @MockitoBean
    private DeleteClaimUseCase deleteClaimUseCase;

    @MockitoBean
    private ClaimWebMapper mapper;

    private UUID claimId;
    private UUID customerId;
    private UUID policyId;
    private Claim claim;
    private ClaimResponse claimResponse;

    @BeforeEach
    void setUp() {
        claimId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        policyId = UUID.randomUUID();

        claim = Claim.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("Dental care")
                .amount(BigDecimal.valueOf(200.0))
                .claimStatus(ClaimStatus.PENDING)
                .build();

        claimResponse = ClaimResponse.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType("HEALTH")
                .description("Dental care")
                .amount(BigDecimal.valueOf(200.0))
                .claimStatus("PENDING")
                .build();
    }

    @Test
    @WithMockUser(roles = "ANALYSIS")
    void createClaim_Success() {
        ClaimRequest request = ClaimRequest.builder()
                .customerId(customerId)
                .policyId(policyId)
                .claimType("HEALTH")
                .description("Dental care")
                .amount(BigDecimal.valueOf(200.0))
                .build();

        when(mapper.toDomain(any(ClaimRequest.class))).thenReturn(claim);
        when(createClaimUseCase.create(any(Claim.class))).thenReturn(Mono.just(claim));
        when(mapper.toResponse(any(Claim.class))).thenReturn(claimResponse);

        webTestClient.mutateWith(csrf())
                .post()
                .uri("/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(claimId.toString())
                .jsonPath("$.claimStatus").isEqualTo("PENDING");
    }

    @Test
    @WithMockUser(roles = "ANALYSIS")
    void getClaimById_Success() {
        when(findClaimUseCase.findById(claimId)).thenReturn(Mono.just(claim));
        when(mapper.toResponse(claim)).thenReturn(claimResponse);

        webTestClient.get()
                .uri("/claims/{id}", claimId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(claimId.toString());
    }

    @Test
    @WithMockUser(roles = "ANALYSIS")
    void getClaimById_NotFound() {
        when(findClaimUseCase.findById(claimId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/claims/{id}", claimId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithMockUser(roles = "ANALYSIS")
    void getAllClaims_Success() {
        when(findClaimUseCase.findAll()).thenReturn(Flux.just(claim));
        when(mapper.toResponse(claim)).thenReturn(claimResponse);

        webTestClient.get()
                .uri("/claims")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(claimId.toString());
    }

    @Test
    @WithMockUser(roles = "ANALYSIS")
    void updateClaim_Success() {
        ClaimRequest request = ClaimRequest.builder()
                .customerId(customerId)
                .policyId(policyId)
                .claimType("HEALTH")
                .description("Updated care")
                .amount(BigDecimal.valueOf(250.0))
                .build();

        Claim updatedClaim = Claim.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType(ClaimType.HEALTH)
                .description("Updated care")
                .amount(BigDecimal.valueOf(250.0))
                .claimStatus(ClaimStatus.APPROVED)
                .build();

        ClaimResponse updatedResponse = ClaimResponse.builder()
                .id(claimId)
                .customerId(customerId)
                .policyId(policyId)
                .claimType("HEALTH")
                .description("Updated care")
                .amount(BigDecimal.valueOf(250.0))
                .claimStatus("APPROVED")
                .build();

        when(mapper.toDomain(any(ClaimRequest.class))).thenReturn(updatedClaim);
        when(updateClaimUseCase.update(eq(claimId), any(Claim.class))).thenReturn(Mono.just(updatedClaim));
        when(mapper.toResponse(updatedClaim)).thenReturn(updatedResponse);

        webTestClient.mutateWith(csrf())
                .put()
                .uri("/claims/{id}", claimId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.description").isEqualTo("Updated care")
                .jsonPath("$.claimStatus").isEqualTo("APPROVED");
    }

    @Test
    @WithMockUser(roles = "ANALYSIS")
    void deleteClaim_Success() {
        when(deleteClaimUseCase.delete(claimId)).thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .delete()
                .uri("/claims/{id}", claimId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(roles = "USER") // Incorrect role
    void getClaimById_Forbidden() {
        when(findClaimUseCase.findById(claimId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/claims/{id}", claimId)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void getClaimById_Unauthorized() {
        webTestClient.get()
                .uri("/claims/{id}", claimId)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
