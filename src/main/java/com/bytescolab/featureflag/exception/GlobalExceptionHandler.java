package com.bytescolab.featureflag.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@code GlobalExceptionHandler} es un manejador centralizado de excepciones
 * para toda la aplicación. Captura diferentes tipos de errores lanzados en los
 * controladores y devuelve una respuesta JSON estructurada con información
 * detallada del error.
 *
 * <p>Incluye el manejo de excepciones personalizadas ({@link ApiException}),
 * validaciones, credenciales inválidas, errores de parsing de JSON y problemas
 * de seguridad como accesos denegados.</p>
 *
 * <p>Cada excepción se traduce a una respuesta {@link ResponseEntity}
 * con un código HTTP apropiado y un cuerpo estándar que incluye:
 * <ul>
 *     <li>{@code timestamp} → Momento del error.</li>
 *     <li>{@code status} → Código HTTP.</li>
 *     <li>{@code error} → Descripción del error HTTP.</li>
 *     <li>{@code message} → Mensaje de error legible.</li>
 *     <li>{@code path} → Path de la petición.</li>
 * </ul>
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String BAD_REQUEST = "Bad Request";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String STATUS = "status";
    public static final String TIMESTAMP = "timestamp";

    /**
     * Maneja las excepciones personalizadas {@link ApiException}.
     *
     * @param exception excepción capturada
     * @param request   contexto de la petición
     * @return respuesta estructurada con el error
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException exception, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, getStatusForCode(exception.getCode()).value());
        body.put(ERROR, getErrorName(getStatusForCode(exception.getCode())));
        body.put("code", exception.getCode());
        body.put(MESSAGE, exception.getMessage());
        body.put("path", request.getDescription(false));
        return new ResponseEntity<>(body, getStatusForCode(exception.getCode()));
    }

    /**
     * Devuelve el {@link HttpStatus} adecuado en base al código de error.
     *
     * @param code código de error interno
     * @return estado HTTP correspondiente
     */
    private HttpStatus getStatusForCode(String code) {
        if (code.startsWith("AUTH_") || code.contains("Internal")) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Obtiene la descripción textual del estado HTTP.
     *
     * @param status código de estado
     * @return cadena con el nombre del error
     */
    private String getErrorName(HttpStatus status) {
        return status.getReasonPhrase();
    }

    /**
     * Maneja errores de validación de argumentos en los controladores
     * (ej. @Valid fallido).
     *
     * @param ex excepción con los errores de validación
     * @return respuesta con los campos inválidos y mensajes
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fields.putIfAbsent(fe.getField(), fe.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, BAD_REQUEST);
        body.put(MESSAGE, "Errores de validación en la solicitud");
        body.put("errors", fields);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de tipo {@link IllegalArgumentException}.
     *
     * @param ex excepción capturada
     * @return respuesta con mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArg(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, BAD_REQUEST);
        body.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas {@link RuntimeException}.
     *
     * @param ex      excepción capturada
     * @param request contexto de la petición
     * @return respuesta con error interno del servidor
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(ERROR, "Internal Server Error");
        body.put(MESSAGE, ex.getMessage());
        body.put("path", request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores de autenticación cuando las credenciales son inválidas.
     *
     * @param ex      excepción capturada
     * @param request contexto de la petición
     * @return respuesta con estado 401 Unauthorized
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, HttpStatus.UNAUTHORIZED.value());
        body.put(ERROR, "Unauthorized");
        body.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja errores de parseo JSON en la petición.
     * Detecta valores inválidos para enums y devuelve mensajes personalizados.
     *
     * @param ex      excepción de lectura
     * @param request contexto de la petición
     * @return respuesta con error 400 y detalle del campo inválido
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().stream()
                    .map(com.fasterxml.jackson.databind.JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("valor");

            String acceptedValues = "";
            if (ife.getTargetType().isEnum()) {
                Object[] constants = ife.getTargetType().getEnumConstants();
                acceptedValues = " Valores permitidos: " + Arrays.toString(constants);
            }

            String msg = String.format(
                    "El campo '%s' contiene un valor inválido: '%s'.%s",
                    fieldName,
                    ife.getValue(),
                    acceptedValues
            );

            Map<String, Object> body = new HashMap<>();
            body.put(TIMESTAMP, Instant.now());
            body.put(STATUS, HttpStatus.BAD_REQUEST.value());
            body.put(ERROR, BAD_REQUEST);
            body.put(MESSAGE, msg);
            body.put("path", request.getDescription(false).replace("uri=", ""));
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, BAD_REQUEST);
        body.put(MESSAGE, "JSON mal formado o incompatible");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja intentos de acceso sin permisos suficientes.
     *
     * @param ex      excepción capturada
     * @param request contexto de la petición
     * @return respuesta con estado 403 Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, HttpStatus.FORBIDDEN.value());
        body.put(ERROR, "Forbidden");
        body.put(MESSAGE, "Acceso denegado: no tienes permisos suficientes");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
