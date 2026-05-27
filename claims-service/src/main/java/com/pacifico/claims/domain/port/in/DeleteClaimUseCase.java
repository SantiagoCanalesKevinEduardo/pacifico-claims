package com.pacifico.claims.domain.port.in;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeleteClaimUseCase {
    Mono<Void> delete(UUID id);
}
