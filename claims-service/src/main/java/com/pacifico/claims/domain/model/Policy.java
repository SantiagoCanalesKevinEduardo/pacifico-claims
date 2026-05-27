package com.pacifico.claims.domain.model;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record Policy(
    UUID id,
    UUID customerId,
    String policyNumber,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal coverageAmount
) {}
