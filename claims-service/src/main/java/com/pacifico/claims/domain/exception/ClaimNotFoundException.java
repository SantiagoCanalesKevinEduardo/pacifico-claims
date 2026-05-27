package com.pacifico.claims.domain.exception;

public class ClaimNotFoundException extends BusinessException {
    public ClaimNotFoundException(Long id) {
        super("Claim not found with id: " + id);
    }
}
