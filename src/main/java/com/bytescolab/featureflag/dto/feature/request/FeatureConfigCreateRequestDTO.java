package com.bytescolab.featureflag.dto.feature.request;

import com.bytescolab.featureflag.model.enums.Environment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigCreateRequestDTO {

    @NotNull(message = "El environment es obligatorio")
    @Schema(example = "DEV", description = "Entorno al que aplica la configuración (DEV, STAGING, PROD)")
    private Environment environment;

    @Schema(example = "cliente123", description = "Identificador del cliente (opcional)")
    private String clientId;

    @NotNull(message = "enabled es obligatorio")
    @Schema(example = "true", description = "Indica si la feature esta" +
            " activada o desactivada en este cliente/entorno")
    private Boolean enabled;
}
