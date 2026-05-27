package com.pacifico.claims.domain.exception;

import java.util.UUID;

public class PolicyNotFoundException extends BusinessException {
    public PolicyNotFoundException(UUID id) {
        super("Policy not found with id: " + id);
    }
}
