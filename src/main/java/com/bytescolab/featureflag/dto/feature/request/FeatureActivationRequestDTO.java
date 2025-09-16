package com.bytescolab.featureflag.dto.feature.request;

import com.bytescolab.featureflag.model.enums.Environment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureActivationRequestDTO {

    @NotNull(message = "Environment es necesario")
    @Schema(example = "DEV", description = "Entorno al que aplica la configuración (DEV, STAGING, PROD)")
    private Environment environment;

    @NotBlank (message = "clientId es necesario")
    @Schema(example = "cliente123", description = "Nombre del cliente")
    private String clientId;

    @Schema(example = "true", description = "Valor positivo para activación de la feature")
    private Boolean enabled;
}
