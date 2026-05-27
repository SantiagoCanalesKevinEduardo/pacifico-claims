package com.pacifico.claims.infrastructure.adapters.out.persistence.repository;

import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.ClaimEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ClaimR2dbcRepository extends ReactiveCrudRepository<ClaimEntity, UUID> {
}
