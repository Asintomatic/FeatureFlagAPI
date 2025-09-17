package com.bytescolab.featureflag.dto.feature.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta que representa
 * el detalle completo de una {@code Feature}.
 *
 * <p>Incluye información básica como identificador, nombre,
 * descripción y si está activada por defecto, además de
 * la lista de configuraciones específicas por entorno
 * y cliente ({@link FeatureConfigResponseDTO}).</p>
 *
 * <p>Este DTO suele devolverse en las operaciones de consulta
 * de una feature en particular o al crear una nueva.</p>
 *
 * @author Bytes
 * @see FeatureConfigResponseDTO
 * @see com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureDetailResponseDTO {

    /**
     * Identificador único de la feature.
     * Solo lectura, generado por el sistema.
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    /**
     * Nombre único de la feature.
     */
    private String name;

    /**
     * Descripción de la feature.
     */
    private String description;

    /**
     * Indica si la feature está activada por defecto.
     */
    private Boolean enabledByDefault;

    /**
     * Lista de configuraciones asociadas a esta feature,
     * diferenciadas por entorno y cliente.
     */
    private List<FeatureConfigResponseDTO> configs;
}
