package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para gestionar la persistencia de entidades {@link User}.
 *
 * <p>Proporciona métodos de consulta específicos para trabajar con usuarios,
 * principalmente búsquedas por nombre de usuario y validación de existencia.</p>
 *
 * <h2>Consultas personalizadas:</h2>
 * <ul>
 *   <li>{@link #findByUsername(String)}: Recupera un usuario a partir de su nombre de usuario.</li>
 *   <li>{@link #existsByUsername(String)}: Verifica si ya existe un usuario con el nombre dado.</li>
 * </ul>
 *
 * <p>Al extender {@link JpaRepository}, hereda todos los métodos CRUD estándar
 * como {@code save}, {@code findById}, {@code delete}, {@code findAll}, etc.</p>
 *
 * @author Bytes
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param name el nombre de usuario a buscar.
     * @return un {@link Optional} que contiene el {@link User} si se encuentra,
     *         o vacío si no existe.
     */
    Optional<User> findByUsername(String name);

    /**
     * Verifica si ya existe un usuario con el nombre de usuario especificado.
     *
     * @param username el nombre de usuario a comprobar.
     * @return {@code true} si existe un usuario con ese nombre, {@code false} en caso contrario.
     */
    boolean existsByUsername(String username);
}
