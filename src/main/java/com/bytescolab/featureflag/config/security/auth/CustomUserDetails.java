package com.bytescolab.featureflag.config.security.auth;

import com.bytescolab.featureflag.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
/**
 * Implementación personalizada de {@link UserDetails} que adapta la entidad {@link User}
 * al modelo de seguridad de Spring Security.
 *
 * <p>Esta clase se utiliza por {@code UserDetailsService} y Spring Security para
 * autenticar y autorizar usuarios dentro del sistema. Convierte los datos de
 * {@link User} (como nombre, contraseña y rol) en un objeto compatible con
 * el contexto de seguridad.</p>
 *
 * <h2>Características principales:</h2>
 * <ul>
 *   <li>Asocia un {@link User} con los detalles de autenticación.</li>
 *   <li>Convierte el rol del usuario en una {@link GrantedAuthority} con el prefijo {@code ROLE_}.</li>
 *   <li>Proporciona acceso a username y password según el contrato de Spring Security.</li>
 * </ul>
 *
 * <p><b>Nota:</b> Los métodos relacionados con la caducidad y el bloqueo de la cuenta
 * utilizan las implementaciones por defecto de {@link UserDetails} (no implementados aquí),
 * lo que implica que la cuenta siempre estará habilitada salvo que se amplíe.</p>
 *
 * @author Bytes
 * @see User
 * @see org.springframework.security.core.userdetails.UserDetails
 */
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    /**
     * Entidad de usuario asociada a este detalle de autenticación.
     */
    private User user;

    /**
     * Devuelve las autoridades concedidas al usuario.
     * En este caso, se construye a partir del rol definido en {@link User}.
     *
     * @return una colección con la autoridad {@code ROLE_<ROL>}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return la contraseña encriptada del usuario.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Devuelve el nombre de usuario.
     *
     * @return el username único asociado al usuario.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}

