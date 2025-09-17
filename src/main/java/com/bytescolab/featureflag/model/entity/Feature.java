package com.bytescolab.featureflag.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;
/**
 * Entidad JPA que representa una <b>Feature Flag</b> dentro del sistema.
 *
 * <p>Una Feature Flag es una funcionalidad que puede activarse o desactivarse dinámicamente
 * según el entorno o el cliente. Cada feature puede tener configuraciones asociadas
 * en la entidad {@link FeatureConfig}.</p>
 *
 * <p>La entidad se persiste en la tabla <b>features</b> y se identifica de manera única
 * mediante un {@link UUID} generado automáticamente.</p>
 *
 * <h2>Campos principales:</h2>
 * <ul>
 *   <li><b>id</b>: Identificador único de la feature.</li>
 *   <li><b>name</b>: Nombre único de la feature (no puede ser nulo ni repetido).</li>
 *   <li><b>description</b>: Descripción breve de la feature.</li>
 *   <li><b>enabledByDefault</b>: Define si la feature está activa por defecto en ausencia de configuraciones.</li>
 *   <li><b>configs</b>: Lista de configuraciones asociadas por cliente y entorno.</li>
 * </ul>
 *
 * <p>Se aplica <b>cascade ALL</b> en la relación con {@link FeatureConfig},
 * lo que significa que las operaciones sobre la feature (persistencia, borrado, etc.)
 * se propagan a sus configuraciones.</p>
 *
 * <p>Incluye un método de conveniencia {@link #isEnabledByDefault()} para obtener el valor del flag por defecto.</p>
 *
 * @author Bytes
 * @see FeatureConfig
 */
@Entity
@Data
@Table(name = "features")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "configs")
public class Feature {

    /** Identificador único de la feature (UUID generado automáticamente). */
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    /** Nombre único de la feature. No puede ser nulo ni duplicado. */
    @Column(nullable = false, unique = true)
    private String name;

    /** Descripción breve de la feature. No puede ser nula. */
    @Column(nullable = false)
    private String description;

    /** Indica si la feature está habilitada por defecto en ausencia de configuraciones específicas. */
    private Boolean enabledByDefault;

    /** Lista de configuraciones asociadas a esta feature, organizadas por cliente y entorno. */
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private List<FeatureConfig> configs;

    /**
     * Retorna si la feature está activada por defecto.
     *
     * @return {@code true} si la feature está habilitada por defecto,
     *         {@code false} en caso contrario.
     */
    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }
}
