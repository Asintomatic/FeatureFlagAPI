package com.bytescolab.featureflag.dto.feature.request;

import com.bytescolab.featureflag.model.enums.Environment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de configuraciones
 * asociadas a una {@code Feature}.
 *
 * <p>Permite definir si una feature está habilitada o deshabilitada
 * en un entorno específico ({@link com.bytescolab.featureflag.model.enums.Environment})
 * y opcionalmente asociarla a un cliente concreto.</p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO
 * @see com.bytescolab.featureflag.model.entity.FeatureConfig
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigCreateRequestDTO {

    /**
     * Entorno al que aplica la configuración de la feature.
     * Puede ser DEV, STAGING o PROD.
     */
    @NotNull(message = "El environment es obligatorio")
    @Schema(example = "DEV", description = "Entorno al que aplica la configuración (DEV, STAGING, PROD)")
    private Environment environment;

    /**
     * Identificador del cliente al que aplica la configuración.
     * Este campo es opcional; si no se especifica, la configuración
     * se aplicará de forma general al entorno.
     */
    @Schema(example = "cliente123", description = "Identificador del cliente (opcional)")
    private String clientId;

    /**
     * Indica si la feature está habilitada ({@code true})
     * o deshabilitada ({@code false}) para este cliente/entorno.
     */
    @NotNull(message = "enabled es obligatorio")
    @Schema(example = "true", description = "Indica si la feature está activada o desactivada en este cliente/entorno")
    private Boolean enabled;
}
