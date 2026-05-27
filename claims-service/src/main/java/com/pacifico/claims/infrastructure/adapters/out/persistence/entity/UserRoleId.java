package com.pacifico.claims.infrastructure.adapters.out.persistence.entity;

import java.io.Serializable;
import java.util.UUID;

public record UserRoleId(UUID userId, UUID roleId) implements Serializable {
}
