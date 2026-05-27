package com.pacifico.claims.infrastructure.adapters.out.persistence.repository;

import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.RoleEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface RoleR2dbcRepository extends ReactiveCrudRepository<RoleEntity, UUID> {
    
    @Query("SELECT r.* FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId")
    Flux<RoleEntity> findRolesByUserId(UUID userId);
}
