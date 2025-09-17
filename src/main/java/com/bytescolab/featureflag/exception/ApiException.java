package com.bytescolab.featureflag.exception;

import lombok.Getter;

/**
 * Excepción personalizada de la aplicación que extiende {@link RuntimeException}.
 *
 * <p>Se utiliza para representar errores controlados dentro de la lógica
 * de negocio de la API de Feature Flags, permitiendo devolver un código
 * de error semántico junto con el mensaje asociado.</p>
 *
 * <p>Este enfoque permite al {@link com.bytescolab.featureflag.exception.GlobalExceptionHandler}
 * transformar la excepción en una respuesta HTTP estandarizada.</p>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.exception.GlobalExceptionHandler
 * @see com.bytescolab.featureflag.exception.ErrorCodes
 */
@Getter
public class ApiException extends RuntimeException {

    /**
     * Código de error asociado a la excepción.
     *
     * <p>Debe corresponder con alguno de los valores definidos en
     * {@link com.bytescolab.featureflag.exception.ErrorCodes}.</p>
     */
    private final String code;

    /**
     * Crea una nueva instancia de {@code ApiException}.
     *
     * @param code    código de error semántico (ej. {@code FEATURE_NOT_FOUND}).
     * @param message mensaje descriptivo del error.
     */
    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }
}
