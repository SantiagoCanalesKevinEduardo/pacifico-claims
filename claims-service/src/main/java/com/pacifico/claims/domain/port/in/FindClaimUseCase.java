package com.pacifico.claims.domain.port.in;

import com.pacifico.claims.domain.model.Claim;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FindClaimUseCase {
    Mono<Claim> findById(UUID id);
    Flux<Claim> findAll();
}
