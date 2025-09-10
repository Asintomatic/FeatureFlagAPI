package com.bytescolab.featureflag.dto.feature.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureCreateRequestDTO {

    @NotBlank(message = "El nombre de la feature es obligatorio")
    @Schema(example = "dark_mode", description = "Nombre único de la feature")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(example = "Modo oscuro para el frontend", description = "Descripción de la feature")
    private String description;

    @NotNull(message = "enabledByDefault es obligatorio")
    @Schema(example = "true", description = "Indica si la feature está activa por defecto")
    private Boolean enabledByDefault;

    @Schema(description = "Configuraciones por entorno/cliente para esta feature")
    private List<FeatureConfigCreateRequestDTO> configs;
}