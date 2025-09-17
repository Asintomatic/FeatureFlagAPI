package com.bytescolab.featureflag.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud de inicio de sesión de un usuario.
 * <p>
 * Este objeto se utiliza en el endpoint de autenticación para recibir las credenciales
 * del usuario que desea acceder al sistema. Ambas propiedades son obligatorias y
 * están validadas mediante anotaciones de Bean Validation.
 * </p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.controller.AuthController
 */
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
