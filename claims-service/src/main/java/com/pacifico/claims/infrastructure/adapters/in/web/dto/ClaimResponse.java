package com.pacifico.claims.infrastructure.adapters.in.web.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ClaimResponse(
    UUID id,
    UUID customerId,
    UUID policyId,
    String claimType,
    String description,
    BigDecimal amount,
    String claimStatus
) {}
