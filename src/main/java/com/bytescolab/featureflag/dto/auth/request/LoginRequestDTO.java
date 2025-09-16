package com.bytescolab.featureflag.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank (message = "Username es necesario")
    @Schema(example = "Prueba", description = "Nombre del usuario")
    private String username;

    @NotBlank (message = "Password es necesario")
    @Schema(example = "Prueba1!", description = "Contraseña del usuario")
    private String password;
}
