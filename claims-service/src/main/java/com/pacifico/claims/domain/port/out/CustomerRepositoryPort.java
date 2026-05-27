package com.pacifico.claims.domain.port.out;

import com.pacifico.claims.domain.model.Customer;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerRepositoryPort {
    Mono<Customer> findById(UUID id);
}
