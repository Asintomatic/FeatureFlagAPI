package com.bytescolab.featureflag.repository.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud de registro de un nuevo usuario.
 * <p>
 * Este objeto se utiliza en el endpoint de autenticación para recibir los datos
 * necesarios al crear una cuenta. Contiene validaciones de Bean Validation para
 * garantizar que los datos cumplan con los requisitos de seguridad.
 * </p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.controller.AuthController
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    /**
     * Nombre de usuario único para el registro.
     * <p>
     * Ejemplo: <i>"Prueba"</i>
     * </p>
     */
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    @Schema(example = "Prueba", description = "Nombre del usuario")
    private String username;

    /**
     * Contraseña del usuario con validaciones de seguridad:
     * <ul>
     *   <li>Entre 6 y 16 caracteres.</li>
     *   <li>Debe incluir al menos una letra minúscula.</li>
     *   <li>Debe incluir al menos una letra mayúscula.</li>
     *   <li>Debe incluir al menos un número.</li>
     *   <li>Debe incluir al menos un carácter especial.</li>
     * </ul>
     * <p>
     * Ejemplo: <i>"Prueba1!"</i>
     * </p>
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 16, message = "La contraseña debe tener entre 6 y 16 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{6,16}$",
            message = "La contraseña debe contener al menos una minúscula, una mayúscula, un número y un carácter especial"
    )
    @Schema(example = "Prueba1!", description = "Contraseña del usuario")
    private String password;
}
