package com.pacifico.claims.infrastructure.adapters.out.persistence.entity;

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
import java.time.LocalDateTime;
import java.util.UUID;

@Table("policies")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PolicyEntity extends AuditableEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private UUID customerId;
    private String policyNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal coverageAmount;

    @Override
    @Transient
    public boolean isNew() {
        return this.id == null;
    }
}
