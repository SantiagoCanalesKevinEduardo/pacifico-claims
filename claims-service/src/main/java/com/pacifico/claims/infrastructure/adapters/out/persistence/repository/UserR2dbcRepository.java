package com.pacifico.claims.infrastructure.adapters.out.persistence.repository;

import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, UUID> {
    Mono<UserEntity> findByUsername(String username);
}
