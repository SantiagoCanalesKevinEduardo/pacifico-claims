package com.pacifico.claims.infrastructure.adapters.in.web.dto;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email
) {}
