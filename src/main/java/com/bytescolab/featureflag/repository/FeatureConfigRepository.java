package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para gestionar la persistencia de entidades {@link FeatureConfig}.
 *
 * <p>Proporciona métodos de consulta específicos para trabajar con configuraciones
 * de {@link Feature}, filtrando por entorno ({@link Environment}) y cliente.</p>
 *
 * <h2>Consultas personalizadas:</h2>
 * <ul>
 *   <li>{@link #findByFeatureAndEnvironmentAndClientId(Feature, Environment, String)}:
 *       Busca una configuración concreta de un feature para un cliente y entorno específico.</li>
 *   <li>{@link #existsByFeatureAndEnvironmentAndClientId(Feature, Environment, String)}:
 *       Verifica si ya existe una configuración para un feature dado, cliente y entorno.</li>
 *   <li>{@link #findByFeatureIdAndEnvironmentAndClientIdIsNull(UUID, Environment)}:
 *       Busca la configuración por entorno cuando no está asociada a un cliente (configuración global).</li>
 * </ul>
 *
 * <p>Al extender {@link JpaRepository}, hereda todos los métodos CRUD estándar
 * como {@code save}, {@code findById}, {@code delete}, {@code findAll}, etc.</p>
 *
 * @author Bytes
 * @see FeatureConfig
 * @see Feature
 * @see Environment
 */
@Repository
public interface FeatureConfigRepository extends JpaRepository<FeatureConfig, UUID> {

    /**
     * Busca una configuración específica de un feature en un entorno y cliente concretos.
     *
     * @param feature la entidad {@link Feature} asociada.
     * @param environment el entorno en el que aplica la configuración.
     * @param clientId el identificador del cliente.
     * @return un {@link Optional} que contiene la configuración si existe, o vacío en caso contrario.
     */
    Optional<FeatureConfig> findByFeatureAndEnvironmentAndClientId(Feature feature, Environment environment, String clientId);

    /**
     * Verifica si ya existe una configuración para un feature dado en un entorno y cliente específicos.
     *
     * @param feature la entidad {@link Feature} asociada.
     * @param environment el entorno en el que aplica la configuración.
     * @param clientId el identificador del cliente.
     * @return {@code true} si la configuración existe, {@code false} en caso contrario.
     */
    boolean existsByFeatureAndEnvironmentAndClientId(Feature feature, Environment environment, String clientId);

    /**
     * Busca una configuración de feature en un entorno específico sin cliente asociado (configuración global).
     *
     * @param featureId el identificador único de la {@link Feature}.
     * @param env el entorno en el que aplica la configuración.
     * @return un {@link Optional} con la configuración encontrada, o vacío si no existe.
     */
    Optional<FeatureConfig> findByFeatureIdAndEnvironmentAndClientIdIsNull(UUID featureId, Environment env);

}
