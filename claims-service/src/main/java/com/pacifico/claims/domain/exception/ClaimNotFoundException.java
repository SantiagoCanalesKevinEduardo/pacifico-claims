package com.pacifico.claims.domain.exception;

import java.util.UUID;

public class ClaimNotFoundException extends BusinessException {
    public ClaimNotFoundException(UUID id) {
        super("Claim not found with id: " + id);
    }
}
