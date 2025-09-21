package com.bytescolab.featureflag.repository.dto.feature.response;

import com.bytescolab.featureflag.model.enums.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO de respuesta para representar la configuración
 * de una {@code Feature} en un entorno y/o cliente específico.
 *
 * <p>Este objeto se devuelve como parte de las respuestas de API cuando se
 * consultan los detalles de una feature. Proporciona información sobre el
 * estado de activación de la feature en un determinado {@link Environment}
 * y cliente concreto.</p>
 *
 * @author Bytes
 * @see FeatureDetailResponseDTO
 * @see com.bytescolab.featureflag.model.entity.FeatureConfig
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigResponseDTO {

    /**
     * Entorno al que aplica esta configuración (ej. DEV, STAGING, PROD).
     */
    private Environment environment;

    /**
     * Identificador del cliente asociado a esta configuración.
     * Puede ser {@code null} si la configuración aplica a todos los clientes del entorno.
     */
    private String clientId;

    /**
     * Indica si la feature está activada ({@code true}) o desactivada ({@code false})
     * en este cliente/entorno.
     */
    private Boolean enabled;
}
