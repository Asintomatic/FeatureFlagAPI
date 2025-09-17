package com.bytescolab.featureflag.model.enums;

/**
 * Enumeración que define los distintos roles de usuario dentro del sistema.
 *
 * <p>Los roles determinan los permisos y niveles de acceso a los endpoints
 * y funcionalidades de la aplicación. Se integran con Spring Security
 * mediante anotaciones como {@code @PreAuthorize} para aplicar control de acceso.</p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.model.entity.User
 * @see org.springframework.security.access.prepost.PreAuthorize
 */
public enum Role {
    /** Rol con permisos de administrador y acceso completo. */
    ADMIN,

    /** Rol estándar de usuario con permisos limitados. */
    USER

}
