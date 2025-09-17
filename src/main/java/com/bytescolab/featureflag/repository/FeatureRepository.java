package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para gestionar la persistencia de entidades {@link Feature}.
 *
 * <p>Proporciona métodos de consulta específicos para trabajar con las features,
 * permitiendo búsquedas por nombre y estado de activación por defecto.</p>
 *
 * <h2>Consultas personalizadas:</h2>
 * <ul>
 *   <li>{@link #existsByName(String)}: Verifica si existe una feature con un nombre concreto.</li>
 *   <li>{@link #findByEnabledByDefault(Boolean)}: Recupera todas las features activadas o desactivadas por defecto.</li>
 *   <li>{@link #findByNameContainingIgnoreCase(String)}: Busca features cuyo nombre contenga un texto, sin distinguir mayúsculas/minúsculas.</li>
 *   <li>{@link #findByEnabledByDefaultAndNameContainingIgnoreCase(Boolean, String)}:
 *       Busca features filtrando tanto por estado de activación por defecto como por nombre parcial.</li>
 * </ul>
 *
 * <p>Al extender {@link JpaRepository}, hereda todos los métodos CRUD estándar
 * como {@code save}, {@code findById}, {@code delete}, {@code findAll}, etc.</p>
 *
 * @author Bytes
 * @see Feature
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {

    /**
     * Verifica si ya existe una feature con el nombre dado.
     *
     * @param name el nombre de la feature a comprobar.
     * @return {@code true} si la feature existe, {@code false} en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Recupera todas las features filtradas por su estado de activación por defecto.
     *
     * @param enabled {@code true} para obtener solo las activadas por defecto, {@code false} en caso contrario.
     * @return una lista de {@link Feature} que cumplen la condición.
     */
    List<Feature> findByEnabledByDefault(Boolean enabled);

    /**
     * Busca todas las features cuyo nombre contenga el texto especificado, sin distinguir mayúsculas/minúsculas.
     *
     * @param name fragmento del nombre de la feature a buscar.
     * @return una lista de {@link Feature} que coinciden con el criterio.
     */
    List<Feature> findByNameContainingIgnoreCase(String name);

    /**
     * Recupera todas las features que coincidan tanto en su estado de activación por defecto como en el nombre parcial.
     *
     * @param enabled estado de activación por defecto.
     * @param name fragmento del nombre a buscar (ignorando mayúsculas/minúsculas).
     * @return una lista de {@link Feature} que cumplen ambas condiciones.
     */
    List<Feature> findByEnabledByDefaultAndNameContainingIgnoreCase(Boolean enabled, String name);

}
