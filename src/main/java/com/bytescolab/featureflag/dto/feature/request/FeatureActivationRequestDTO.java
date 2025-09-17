package com.bytescolab.featureflag.dto.feature.request;

import com.bytescolab.featureflag.model.enums.Environment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para la activación o desactivación
 * de una {@code Feature} en un cliente y entorno específicos.
 *
 * <p>Este objeto se utiliza en los endpoints de activación y desactivación
 * de features, indicando a qué entorno y cliente aplica la configuración
 * y si la feature debe quedar habilitada o no.</p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.controller.FeatureController
 * @see com.bytescolab.featureflag.service.feature.FeatureService
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureActivationRequestDTO {

    /**
     * Entorno al que aplica la configuración.
     * Ejemplos: DEV, STAGING, PROD.
     */
    @NotNull(message = "Environment es necesario")
    @Schema(example = "DEV", description = "Entorno al que aplica la configuración (DEV, STAGING, PROD)")
    private Environment environment;

    /**
     * Identificador único del cliente.
     */
    @NotBlank(message = "clientId es necesario")
    @Schema(example = "cliente123", description = "Nombre del cliente")
    private String clientId;

    /**
     * Indica si la feature se activa o desactiva.
     * {@code true} → la feature se habilita.
     * {@code false} → la feature se deshabilita.
     */
    @Schema(example = "true", description = "Valor positivo para activación de la feature")
    private Boolean enabled = Boolean.TRUE;

}
