package com.bytescolab.featureflag.dto.feature.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

/**
 * DTO para la creación de una nueva {@code Feature}.
 *
 * <p>Permite definir las propiedades principales de una feature, como su nombre,
 * descripción y si está habilitada por defecto. Además, admite la inclusión
 * de configuraciones específicas por entorno y cliente mediante la lista de
 * {@link FeatureConfigCreateRequestDTO}.</p>
 *
 * <p>Este DTO se utiliza en el endpoint de creación de features y es validado
 * automáticamente por {@code Bean Validation} para garantizar que los datos
 * cumplen las restricciones definidas.</p>
 *
 * @author Bytes
 * @see FeatureConfigCreateRequestDTO
 * @see com.bytescolab.featureflag.model.entity.Feature
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureCreateRequestDTO {

    /**
     * Nombre único de la feature.
     * Debe ser un valor no vacío y sirve como identificador lógico.
     */
    @NotBlank(message = "El nombre de la feature es obligatorio")
    @Schema(example = "dark_mode", description = "Nombre único de la feature")
    private String name;

    /**
     * Descripción detallada de la feature.
     * Ayuda a comprender el propósito y funcionalidad de la misma.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Schema(example = "Modo oscuro para el frontend", description = "Descripción de la feature")
    private String description;

    /**
     * Indica si la feature está activada por defecto.
     * Si no se encuentran configuraciones específicas, este valor será el utilizado.
     */
    @NotNull(message = "enabledByDefault es obligatorio")
    @Schema(example = "true", description = "Indica si la feature está activa por defecto")
    private Boolean enabledByDefault;

    /**
     * Lista opcional de configuraciones específicas para entornos o clientes.
     * Cada elemento de la lista representa un ajuste de activación/desactivación
     * en un {@code Environment} o cliente concreto.
     */
    @Valid
    @Schema(description = "Configuraciones por entorno/cliente para esta feature")
    private List<FeatureConfigCreateRequestDTO> configs;
}
