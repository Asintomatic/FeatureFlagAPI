package com.bytescolab.featureflag.model.entity;

import com.bytescolab.featureflag.model.enums.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que representa a un usuario dentro del sistema de gestión de Feature Flags.
 *
 * <p>La entidad se persiste en la tabla <b>users</b> y contiene la información
 * necesaria para la autenticación y autorización de los usuarios en la aplicación.</p>
 *
 * <h2>Campos principales:</h2>
 * <ul>
 *   <li><b>id</b>: Identificador único del usuario (UUID generado automáticamente).</li>
 *   <li><b>username</b>: Nombre de usuario único utilizado para autenticación.</li>
 *   <li><b>password</b>: Contraseña cifrada del usuario.</li>
 *   <li><b>role</b>: Rol asignado al usuario ({@link com.bytescolab.featureflag.model.enums.Role}),
 *       utilizado para definir sus permisos (ej. ADMIN o USER).</li>
 * </ul>
 *
 * <h2>Características:</h2>
 * <ul>
 *   <li>Implementa {@link Serializable} para permitir la serialización en cachés o sesiones distribuidas.</li>
 *   <li>Utiliza anotaciones de Lombok para generar constructores, getters, setters y métodos utilitarios.</li>
 *   <li>Define una estrategia de generación de UUID como clave primaria.</li>
 * </ul>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.model.enums.Role
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    /** Identificador único del usuario (UUID generado automáticamente). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Nombre único del usuario, utilizado para autenticación. */
    @Column(nullable = false, unique = true)
    private String username;

    /** Contraseña del usuario (almacenada en formato cifrado). */
    @Column(nullable = false)
    private String password;

    /** Rol asignado al usuario, define permisos y accesos en la aplicación. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
