package com.pacifico.claims.infrastructure.adapters.in.web.dto;

public record LoginResponse(
    String token,
    String username
) {}
