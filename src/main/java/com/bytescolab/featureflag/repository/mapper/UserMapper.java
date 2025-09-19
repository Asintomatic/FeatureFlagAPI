package com.bytescolab.featureflag.repository.mapper;


import com.bytescolab.featureflag.repository.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.repository.dto.auth.request.UserRoleUpdateRequestDTO;
import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
/**
 * Clase de utilidad encargada de convertir entre objetos DTO relacionados con usuarios
 * y la entidad {@link User}.
 * <p>
 * Proporciona métodos estáticos para:
 * <ul>
 *   <li>Convertir un {@link RegisterRequestDTO} en una entidad {@link User}.</li>
 *   <li>Aplicar cambios de rol en un {@link User} a partir de un {@link UserRoleUpdateRequestDTO}.</li>
 * </ul>
 *
 * <p>
 * Esta clase está marcada como <b>utility class</b> y no puede ser instanciada.
 * </p>
 *
 * @author Bytes
 * @see User
 * @see RegisterRequestDTO
 * @see UserRoleUpdateRequestDTO
 */
public class UserMapper {

    /**
     * Constructor privado para evitar la instanciación de la clase.
     * Lanza una excepción si se intenta instanciar.
     */
    private UserMapper() {
        throw new ApiException(ErrorCodes.BAD_REQUEST,ErrorCodes.BAD_REQUEST_MSG);
    }

    /**
     * Convierte un {@link RegisterRequestDTO} en una entidad {@link User}.
     * <p>
     * Por defecto, asigna el rol {@link Role#USER} al nuevo usuario.
     * </p>
     *
     * @param dto DTO que contiene la información de registro de un usuario
     * @return una nueva instancia de {@link User} con los datos del DTO
     */
    public static User toEntity(RegisterRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(Role.USER)
                .build();
    }

    /**
     * Aplica una actualización de rol a un {@link User}.
     * <p>
     * Utiliza el valor del DTO {@link UserRoleUpdateRequestDTO} para cambiar el rol
     * del usuario especificado.
     * </p>
     *
     * @param user    la entidad {@link User} a la que se actualizará el rol
     * @param dto     DTO que contiene el nuevo rol a asignar
     * @throws IllegalArgumentException si el rol proporcionado en el DTO no existe en {@link Role}
     */
    public static void applyRoleUpdate(User user, UserRoleUpdateRequestDTO dto) {
        user.setRole(Role.valueOf(dto.getRole()));
    }
}
