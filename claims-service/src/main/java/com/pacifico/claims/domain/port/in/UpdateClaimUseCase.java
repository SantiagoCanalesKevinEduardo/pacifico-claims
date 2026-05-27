package com.pacifico.claims.domain.port.in;

import com.pacifico.claims.domain.model.Claim;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UpdateClaimUseCase {
    Mono<Claim> update(UUID id, Claim claim);
}
