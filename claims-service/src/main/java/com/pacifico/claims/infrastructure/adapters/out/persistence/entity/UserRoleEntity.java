package com.pacifico.claims.infrastructure.adapters.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_roles")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRoleEntity extends AuditableEntity implements Persistable<UserRoleId> {

    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
    private UserRoleId id;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public UserRoleId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
