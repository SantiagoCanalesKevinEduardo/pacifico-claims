package com.pacifico.claims.domain.model;

import lombok.Builder;
import java.util.UUID;

@Builder
public record Customer(
        UUID id,
        String name,
        String email,
        String phone) {
}
