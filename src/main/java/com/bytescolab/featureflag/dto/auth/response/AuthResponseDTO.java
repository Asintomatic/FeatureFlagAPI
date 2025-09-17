package com.bytescolab.featureflag.dto.auth.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO de respuesta para la autenticación de usuarios.
 * <p>
 * Esta clase encapsula la información devuelta al cliente después de un
 * inicio de sesión exitoso. Incluye el token JWT emitido por el sistema,
 * su tipo, la información básica del usuario autenticado y el tiempo de expiración.
 * </p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.controller.AuthController
 * @see com.bytescolab.featureflag.dto.auth.request.LoginRequestDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {

    /**
     * Token JWT generado tras un inicio de sesión exitoso.
     */
    private String accessToken;

    /**
     * Tipo de token devuelto (normalmente "Bearer").
     */
    private String tokenType;

    /**
     * Nombre del usuario autenticado.
     */
    private String username;

    /**
     * Rol asignado al usuario autenticado.
     * Ejemplos: "USER", "ADMIN".
     */
    private String role;

    /**
     * Fecha/hora de expiración del token expresada en timestamp UNIX (segundos).
     */
    private Long expiresAt;
}
