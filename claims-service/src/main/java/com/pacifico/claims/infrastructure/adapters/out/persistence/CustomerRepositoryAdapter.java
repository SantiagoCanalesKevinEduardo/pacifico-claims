package com.pacifico.claims.infrastructure.adapters.out.persistence;

import com.pacifico.claims.domain.model.Customer;
import com.pacifico.claims.domain.port.out.CustomerRepositoryPort;
import com.pacifico.claims.infrastructure.adapters.out.persistence.mapper.ClaimPersistenceMapper;
import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.CustomerR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerR2dbcRepository repository;
    private final ClaimPersistenceMapper mapper;


    @Override
    public Mono<Customer> findById(UUID id) {
        return repository.findById(id)
            .map(mapper::toDomain);
    }
}
