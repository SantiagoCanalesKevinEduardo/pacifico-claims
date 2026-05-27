package com.pacifico.claims.domain.port.out;

import com.pacifico.claims.domain.model.Claim;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClaimRepositoryPort {
    Mono<Claim> save(Claim claim);
    Mono<Claim> findById(UUID id);
    Flux<Claim> findAll();
}
