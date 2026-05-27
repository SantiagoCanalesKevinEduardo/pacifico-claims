package com.pacifico.claims.infrastructure.adapters.in.web.handler;

import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimRequest;
import com.pacifico.claims.infrastructure.adapters.in.web.dto.ClaimResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
public class ClaimRouter {

    @Bean
    @RouterOperations({
        @RouterOperation(path = "/claims", produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST, beanClass = ClaimHandler.class, beanMethod = "createClaim",
            operation = @Operation(operationId = "createClaim", summary = "Create a new claim", tags = { "Claims" },
                requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ClaimRequest.class))),
                responses = {
                    @ApiResponse(responseCode = "201", description = "Claim created successfully", content = @Content(schema = @Schema(implementation = ClaimResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
                })),
        @RouterOperation(path = "/claims/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET, beanClass = ClaimHandler.class, beanMethod = "getClaimById",
            operation = @Operation(operationId = "getClaimById", summary = "Get a claim by its ID", tags = { "Claims" },
                parameters = { @Parameter(name = "id", in = ParameterIn.PATH, description = "The ID of the claim (UUID)", required = true, schema = @Schema(type = "string", format = "uuid")) },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Claim found", content = @Content(schema = @Schema(implementation = ClaimResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Claim not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
                })),
        @RouterOperation(path = "/claims", produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET, beanClass = ClaimHandler.class, beanMethod = "getAllClaims",
            operation = @Operation(operationId = "getAllClaims", summary = "Get all claims", tags = { "Claims" },
                responses = {
                    @ApiResponse(responseCode = "200", description = "List of claims", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClaimResponse.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
                }))
    })
    public RouterFunction<ServerResponse> claimRoutes(ClaimHandler handler) {
        return RouterFunctions.route()
            .path("/claims", builder -> builder
                .POST("", accept(MediaType.APPLICATION_JSON), handler::createClaim)
                .GET("/{id}", handler::getClaimById)
                .GET("", handler::getAllClaims)
            )
            .build();
    }
}
