package com.pacifico.claims.domain.exception;

import java.util.UUID;

public class CustomerNotFoundException extends BusinessException {
    public CustomerNotFoundException(UUID id) {
        super("Customer not found with id: " + id);
    }
}
