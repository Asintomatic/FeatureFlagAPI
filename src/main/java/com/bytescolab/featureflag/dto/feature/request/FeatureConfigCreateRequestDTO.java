package com.bytescolab.featureflag.dto.feature.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigCreateRequestDTO {

    @NotBlank(message = "El environment es obligatorio")
    @Schema(example = "DEV", description = "Entorno al que aplica la configuración (DEV, STAGING, PROD)")
    private String environment;

    @Schema(example = "cliente123", description = "Identificador del cliente (opcional)")
    private String clientId;

    @NotNull(message = "enabled es obligatorio")
    @Schema(example = "true", description = "Indica si la feature esta" +
            " activada o desactivada en este cliente/entorno")
    private Boolean enabled;
}
