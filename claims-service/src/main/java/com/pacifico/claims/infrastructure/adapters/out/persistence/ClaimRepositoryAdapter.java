package com.pacifico.claims.infrastructure.adapters.out.persistence;

import com.pacifico.claims.domain.model.Claim;
import com.pacifico.claims.domain.port.out.ClaimRepositoryPort;
import com.pacifico.claims.infrastructure.adapters.out.persistence.mapper.ClaimPersistenceMapper;
import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.ClaimR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClaimRepositoryAdapter implements ClaimRepositoryPort {

    private final ClaimR2dbcRepository repository;
    private final ClaimPersistenceMapper mapper;


    @Override
    public Mono<Claim> save(Claim claim) {
        return repository.save(mapper.toEntity(claim))
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Claim> findById(UUID id) {
        return repository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Claim> findAll() {
        return repository.findAll()
            .map(mapper::toDomain);
    }
}
