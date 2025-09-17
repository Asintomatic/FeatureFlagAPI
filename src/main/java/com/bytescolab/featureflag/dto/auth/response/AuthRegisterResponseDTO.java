package com.bytescolab.featureflag.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO de respuesta para el registro de un usuario.
 * <p>
 * Esta clase encapsula la información que se devuelve al cliente después de
 * que un usuario se haya registrado correctamente en el sistema.
 * </p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.controller.AuthController
 * @see com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegisterResponseDTO {

    /**
     * Nombre de usuario registrado.
     * Ejemplo: <i>"Prueba"</i>
     */
    private String username;

    /**
     * Rol asignado al usuario después del registro.
     * Posibles valores: <i>USER</i>, <i>ADMIN</i>.
     */
    private String role;
}
