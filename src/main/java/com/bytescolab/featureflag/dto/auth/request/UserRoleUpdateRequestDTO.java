package com.bytescolab.featureflag.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para actualizar
 * el rol de un usuario en el sistema.
 * <p>
 * Este objeto se utiliza en el endpoint {@link com.bytescolab.featureflag.controller.UserController}
 * para cambiar el rol asignado a un usuario existente.
 * </p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.controller.UserController
 * @see com.bytescolab.featureflag.model.enums.Role
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleUpdateRequestDTO {

    /**
     * Rol que se asignará al usuario.
     * <p>
     * Valores permitidos:
     * <ul>
     *   <li><b>USER</b></li>
     *   <li><b>ADMIN</b></li>
     * </ul>
     * </p>
     * Ejemplo: <i>"ADMIN"</i>
     */
    @NotBlank(message = "Role es necesario")
    @Schema(example = "ADMIN", description = "Rol que se aplica al usuario (USER, ADMIN)")
    private String role;
}
