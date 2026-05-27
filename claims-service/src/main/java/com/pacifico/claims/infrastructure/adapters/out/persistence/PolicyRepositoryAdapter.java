package com.pacifico.claims.infrastructure.adapters.out.persistence;

import com.pacifico.claims.domain.model.Policy;
import com.pacifico.claims.domain.port.out.PolicyRepositoryPort;
import com.pacifico.claims.infrastructure.adapters.out.persistence.mapper.ClaimPersistenceMapper;
import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.PolicyR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PolicyRepositoryAdapter implements PolicyRepositoryPort {

    private final PolicyR2dbcRepository repository;
    private final ClaimPersistenceMapper mapper;


    @Override
    public Mono<Policy> findById(UUID id) {
        return repository.findById(id)
            .map(mapper::toDomain);
    }
}
