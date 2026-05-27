package com.pacifico.claims.infrastructure.adapters.in.web.handler;

import com.pacifico.claims.domain.port.in.CreateClaimUseCase;
import com.pacifico.claims.domain.port.in.FindClaimUseCase;
import com.pacifico.claims.domain.port.in.UpdateClaimUseCase;
import com.pacifico.claims.domain.port.in.DeleteClaimUseCase;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.mapper.ClaimWebMapper;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClaimHandler {

    private final CreateClaimUseCase createClaimUseCase;
    private final FindClaimUseCase findClaimUseCase;
    private final UpdateClaimUseCase updateClaimUseCase;
    private final DeleteClaimUseCase deleteClaimUseCase;
    private final ClaimWebMapper mapper;
    private final Validator validator;


    public Mono<ServerResponse> createClaim(ServerRequest request) {
        return request.bodyToMono(ClaimRequest.class)
            .flatMap(req -> {
                var violations = validator.validate(req);
                if (!violations.isEmpty()) {
                    String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg));
                }
                return createClaimUseCase.create(mapper.toDomain(req))
                    .map(mapper::toResponse)
                    .flatMap(res -> ServerResponse.status(HttpStatus.CREATED).bodyValue(res));
            });
    }

    public Mono<ServerResponse> getClaimById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return findClaimUseCase.findById(id)
            .map(mapper::toResponse)
            .flatMap(res -> ServerResponse.ok().bodyValue(res))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAllClaims(ServerRequest request) {
        return findClaimUseCase.findAll()
            .map(mapper::toResponse)
            .collectList()
            .flatMap(res -> ServerResponse.ok().bodyValue(res));
    }

    public Mono<ServerResponse> updateClaim(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return request.bodyToMono(ClaimRequest.class)
            .flatMap(req -> {
                var violations = validator.validate(req);
                if (!violations.isEmpty()) {
                    String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg));
                }
                return updateClaimUseCase.update(id, mapper.toDomain(req))
                    .map(mapper::toResponse)
                    .flatMap(res -> ServerResponse.ok().bodyValue(res));
            });
    }

    public Mono<ServerResponse> deleteClaim(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return deleteClaimUseCase.delete(id)
            .then(ServerResponse.noContent().build());
    }
}
