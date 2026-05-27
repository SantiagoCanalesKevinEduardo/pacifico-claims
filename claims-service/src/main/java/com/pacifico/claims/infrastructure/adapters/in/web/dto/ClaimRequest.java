package com.pacifico.claims.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ClaimRequest(
    @NotNull(message = "Customer ID is required")
    UUID customerId,

    @NotNull(message = "Policy ID is required")
    UUID policyId,

    @NotBlank(message = "Claim type is required")
    String claimType,

    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    BigDecimal amount
) {}
