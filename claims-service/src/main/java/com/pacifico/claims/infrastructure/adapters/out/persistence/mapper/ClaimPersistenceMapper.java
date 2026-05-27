package com.pacifico.claims.infrastructure.adapters.out.persistence.mapper;

import com.pacifico.claims.domain.model.Claim;
import com.pacifico.claims.domain.model.Customer;
import com.pacifico.claims.domain.model.Policy;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.ClaimEntity;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.CustomerEntity;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.PolicyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ClaimPersistenceMapper {

    ClaimEntity toEntity(Claim claim);

    Claim toDomain(ClaimEntity entity);

    CustomerEntity toEntity(Customer customer);

    Customer toDomain(CustomerEntity entity);

    PolicyEntity toEntity(Policy policy);

    Policy toDomain(PolicyEntity entity);
}
