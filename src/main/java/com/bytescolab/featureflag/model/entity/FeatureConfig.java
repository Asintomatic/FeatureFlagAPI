package com.bytescolab.featureflag.model.entity;


import com.bytescolab.featureflag.model.enums.Environment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.util.UUID;
/**
 * Entidad JPA que representa la configuración de una {@link Feature}
 * para un cliente y un entorno específicos.
 *
 * <p>Permite activar o desactivar dinámicamente una feature según:</p>
 * <ul>
 *   <li><b>Environment</b>: El entorno de despliegue ({@link com.bytescolab.featureflag.model.enums.Environment})
 *       como DEV, STAGING o PROD.</li>
 *   <li><b>ClientId</b>: El identificador único de un cliente al que aplica la configuración.</li>
 * </ul>
 *
 * <p>La entidad se persiste en la tabla <b>feature_configs</b> y aplica una restricción
 * de unicidad sobre la combinación de <code>feature_flag_id</code>, <code>environment</code> y <code>client_id</code>,
 * garantizando que no existan configuraciones duplicadas para la misma feature.</p>
 *
 * <h2>Relaciones:</h2>
 * <ul>
 *   <li>{@link Feature}: Relación muchos-a-uno con la entidad Feature,
 *   que indica a qué feature corresponde la configuración.</li>
 * </ul>
 *
 * <h2>Campos principales:</h2>
 * <ul>
 *   <li><b>id</b>: Identificador único de la configuración (UUID).</li>
 *   <li><b>feature</b>: Referencia a la feature asociada.</li>
 *   <li><b>environment</b>: Entorno donde se aplica la configuración.</li>
 *   <li><b>clientId</b>: Identificador del cliente al que aplica.</li>
 *   <li><b>enabled</b>: Estado de la feature (true = activa, false = inactiva).</li>
 * </ul>
 *
 * <p>Incluye el método de conveniencia {@link #isEnabled()} para consultar el estado de la feature.</p>
 *
 * @author Bytes
 * @see Feature
 * @see com.bytescolab.featureflag.model.enums.Environment
 */
@Entity
@Data
@Table(
        name = "feature_configs",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"feature_flag_id", "environment", "client_id"})}
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "feature")
public class FeatureConfig {

    /** Identificador único de la configuración (UUID generado automáticamente). */
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    /** Feature a la que pertenece esta configuración (relación ManyToOne). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    /** Entorno donde se aplica esta configuración (DEV, STAGING, PROD, etc.). */
    @Column(name = "environment", nullable = false)
    @Enumerated(EnumType.STRING)
    private Environment environment;

    /** Identificador del cliente al que aplica la configuración. */
    @Column(name = "client_id", nullable = false)
    private String clientId;

    /** Estado de la feature en esta configuración: true = activa, false = inactiva. */
    @Column(nullable = false)
    private Boolean enabled = false;

    /**
     * Devuelve si la feature está habilitada en esta configuración.
     *
     * @return {@code true} si la feature está activa, {@code false} en caso contrario.
     */
    public boolean isEnabled() {
        return enabled;
    }
}
