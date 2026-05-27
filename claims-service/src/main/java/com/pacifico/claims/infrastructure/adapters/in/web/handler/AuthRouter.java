package com.pacifico.claims.infrastructure.adapters.in.web.handler;

import com.pacifico.claims.infrastructure.adapters.in.web.dto.LoginRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.LoginResponse;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.RegisterRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AuthRouter {

    @Bean
    @RouterOperations({
        @RouterOperation(path = "/auth/login", produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST, beanClass = AuthHandler.class, beanMethod = "login",
            operation = @Operation(operationId = "login", summary = "User login", tags = { "Authentication" },
                requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = LoginRequest.class))),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
                })),
        @RouterOperation(path = "/auth/register", produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST, beanClass = AuthHandler.class, beanMethod = "register",
            operation = @Operation(operationId = "register", summary = "Register new user", tags = { "Authentication" },
                requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterRequest.class))),
                responses = {
                    @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
                }))
    })
    public RouterFunction<ServerResponse> authRoutes(AuthHandler handler) {
        return RouterFunctions.route()
            .path("/auth", builder -> builder
                .POST("/login", accept(MediaType.APPLICATION_JSON), handler::login)
                .POST("/register", accept(MediaType.APPLICATION_JSON), handler::register)
            )
            .build();
    }
}
