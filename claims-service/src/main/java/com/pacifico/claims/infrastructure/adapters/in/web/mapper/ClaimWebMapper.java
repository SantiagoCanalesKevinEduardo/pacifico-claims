package com.pacifico.claims.infrastructure.adapters.in.web.mapper;

import com.pacifico.claims.domain.model.Claim;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ClaimWebMapper {

    ClaimResponse toResponse(Claim claim);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "claimStatus", ignore = true)
    Claim toDomain(ClaimRequest request);
}
