package com.pacifico.claims.domain.port.in;

import com.pacifico.claims.domain.model.Claim;
import reactor.core.publisher.Mono;

public interface CreateClaimUseCase {
    Mono<Claim> create(Claim claim);
}
