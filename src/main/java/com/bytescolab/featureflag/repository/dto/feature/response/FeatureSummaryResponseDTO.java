package com.bytescolab.featureflag.repository.dto.feature.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
/**
 * DTO de respuesta que representa
 * un resumen básico de una {@code Feature}.
 *
 * <p>Incluye únicamente la información esencial:
 * identificador único, nombre y descripción.
 * Se utiliza principalmente en listados o consultas
 * donde no es necesario devolver las configuraciones
 * por entorno/cliente.</p>

 * @author Bytes
 * @see FeatureDetailResponseDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class FeatureSummaryResponseDTO {

    /**
     * Identificador único de la feature.
     * Solo lectura, generado por el sistema.
     */
    private UUID id;

    /**
     * Nombre único de la feature.
     */
    private String name;

    /**
     * Descripción de la feature.
     */
    private String description;
}
