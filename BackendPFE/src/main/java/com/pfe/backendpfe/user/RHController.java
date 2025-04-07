package com.pfe.backendpfe.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rh")
@Tag(name = "Management", description = "Endpoints for management operations")
@PreAuthorize("hasRole('RH')")

public class RHController {

    @Operation(
            summary = "Get endpoint for manager",
            description = "Retrieves management information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String get(Authentication authentication) {
        System.out.println("Utilisateur authentifié : " + authentication.getName());

        return "GET:: management controller";

    }

    @Operation(
            summary = "Post endpoint for manager",
            description = "Creates a management resource",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String post() {
        return "POST:: management controller";
    }

    @Operation(
            summary = "Put endpoint for manager",
            description = "Updates a management resource",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated Successfully"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @PutMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String put() {
        return "PUT:: management controller";
    }

    @Operation(
            summary = "Delete endpoint for manager",
            description = "Deletes a management resource",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deleted Successfully"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @DeleteMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete() {
        return "DELETE:: management controller";
    }

}
