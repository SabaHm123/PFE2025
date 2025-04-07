package com.pfe.backendpfe.auth;

import com.pfe.backendpfe.activity.Activite;
import com.pfe.backendpfe.role.RoleName;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class RegistrationRequest {



    @Email(message = "Email is not formatted ")
    @NotEmpty(message = " Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @Size(min =8, message = "Password should be 8 characters long minimum")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial"
    )
    @NotEmpty(message = " Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    private String password;
    private RoleName role;
    private Integer activiteId;
    @ManyToOne
    private Activite activite;
}
