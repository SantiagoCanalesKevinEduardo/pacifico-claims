package com.pacifico.claims.infrastructure.adapters.out.persistence.repository;

import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.UserRoleEntity;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.UserRoleId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRoleR2dbcRepository extends ReactiveCrudRepository<UserRoleEntity, UserRoleId> {
}
