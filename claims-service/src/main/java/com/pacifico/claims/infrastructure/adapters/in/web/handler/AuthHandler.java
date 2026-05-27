package com.pacifico.claims.infrastructure.adapters.in.web.handler;

import com.pacifico.claims.infrastructure.adapters.in.web.dto.LoginRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.LoginResponse;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.RegisterRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.UserResponse;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.UserRoleEntity;
import com.pacifico.claims.infrastructure.adapters.out.persistence.entity.UserRoleId;
import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.UserR2dbcRepository;
import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.UserRoleR2dbcRepository;
import com.pacifico.claims.infrastructure.security.JwtService;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final UserR2dbcRepository userRepository;
    private final UserRoleR2dbcRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;
    private final Validator validator;


    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(req -> {
                    var violations = validator.validate(req);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input data"));
                    }
                    return userDetailsService.findByUsername(req.username())
                            .flatMap(userDetails -> {
                                if (passwordEncoder.matches(req.password(), userDetails.getPassword())) {
                                    String token = jwtService.generateToken(userDetails);
                                    return ServerResponse.ok()
                                            .bodyValue(new LoginResponse(token, userDetails.getUsername()));
                                }
                                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                        "Invalid username or password"));
                            })
                            .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                    "Invalid username or password")));
                });
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(RegisterRequest.class)
                .flatMap(req -> {
                    var violations = validator.validate(req);
                    if (!violations.isEmpty()) {
                        String errorMsg = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg));
                    }

                    return userRepository.findByUsername(req.username())
                            .flatMap(existing -> Mono
                                    .error(new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists")))
                            .then(Mono.defer(() -> {
                                UserEntity user = UserEntity.builder()
                                        .username(req.username())
                                        .password(passwordEncoder.encode(req.password()))
                                        .email(req.email())
                                        .enabled(true)
                                        .build();
                                return userRepository.save(user);
                            }))
                            .flatMap(savedUser -> {
                                // El rol por defecto ROLE_USER tiene ID cccccccc-cccc-cccc-cccc-cccccccccccc en la base de datos
                                UserRoleEntity userRole = new UserRoleEntity(new UserRoleId(savedUser.getId(), java.util.UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc")),
                                        true);
                                return userRoleRepository.save(userRole)
                                        .thenReturn(savedUser);
                            })
                            .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                                    .bodyValue(new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail())));
                });
    }
}
