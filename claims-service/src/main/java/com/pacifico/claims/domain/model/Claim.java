package com.pacifico.claims.domain.model;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record Claim(
        UUID id,
        UUID customerId,
        UUID policyId,
        ClaimType claimType,
        String description,
        BigDecimal amount,
        ClaimStatus claimStatus) {
}
