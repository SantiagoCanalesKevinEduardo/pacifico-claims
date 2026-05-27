package com.pacifico.claims.infrastructure.adapters.out.persistence.repository;

import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.PolicyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface PolicyR2dbcRepository extends ReactiveCrudRepository<PolicyEntity, UUID> {
}
