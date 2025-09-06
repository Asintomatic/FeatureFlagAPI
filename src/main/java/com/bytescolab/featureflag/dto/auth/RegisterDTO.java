package com.bytescolab.featureflag.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

// Lombok genera getters/setters/constructores (menos código)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterDTO {
    // Validamos entrada desde el propio contrato (Bean Validation)
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 30, message = "username must be 3-50 chars")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 8 chars") // nunca aceptes contraseñas triviales
    private String password;
}
