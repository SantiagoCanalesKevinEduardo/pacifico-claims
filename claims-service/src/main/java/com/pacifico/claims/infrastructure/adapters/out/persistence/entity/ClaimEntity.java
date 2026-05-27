package com.pacifico.claims.infrastructure.adapters.out.persistence.entity;

import com.pacifico.claims.domain.model.ClaimStatus;
import com.pacifico.claims.domain.model.ClaimType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("claims")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClaimEntity extends AuditableEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID policyId;
    private ClaimType claimType;
    private String description;
    private BigDecimal amount;
    private ClaimStatus claimStatus;

    @Override
    @Transient
    public boolean isNew() {
        return this.id == null;
    }
}

