package com.bytescolab.featureflag.model.enums;

/**
 * Enumeración que representa los diferentes entornos de ejecución
 * en los que una feature puede estar configurada dentro del sistema.
 *
 * <p>Se utiliza en las entidades {@link com.bytescolab.featureflag.model.entity.FeatureConfig}
 * y en los DTOs relacionados para definir el ámbito en el que una configuración
 * de feature flag es válida.</p>

 * @author Bytes
 * @see com.bytescolab.featureflag.model.entity.FeatureConfig
 * @see com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO
 */
public enum Environment {
    /** Entorno de desarrollo. */
    DEV,

    /** Entorno de preproducción. */
    STAGING,

    /** Entorno de producción. */
    PROD
}
