package com.pacifico.claims.domain.port.out;

import com.pacifico.claims.domain.model.Policy;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PolicyRepositoryPort {
    Mono<Policy> findById(UUID id);
}
