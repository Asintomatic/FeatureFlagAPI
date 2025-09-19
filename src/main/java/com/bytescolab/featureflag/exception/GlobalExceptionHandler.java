package com.bytescolab.featureflag.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones que unifica todas las respuestas
 * en base a {@link ApiException} y los códigos definidos en {@link ErrorCodes}.
 *
 * <p>Si ocurre cualquier otra excepción no controlada, se transforma
 * automáticamente en un {@link ApiException} genérico con código
 * {@link ErrorCodes#BAD_PARAMS}.</p>
 *
 * <p>En el entorno <b>prod</b>, los mensajes detallados de error y los
 * campos con validaciones fallidas se ocultan para no filtrar información
 * sensible al cliente.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String CODE = "code";
    public static final String PATH = "path";
    public static final String ERRORS = "errors";

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    /**
     * Construye la respuesta estándar de error.
     * Si el entorno es "prod", se ocultan detalles como "message" y "errors" par mejorar la seguridad.
     */
    private Map<String, Object> buildResponse(HttpStatus status, ApiException ex, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, status.value());
        body.put(ERROR, status.getReasonPhrase());
        body.put(CODE, ex.getCode());
        body.put(PATH, path != null ? path.replace("uri=", "") : "");

        if (!"prod".equalsIgnoreCase(activeProfile)) {
            body.put(MESSAGE, ex.getMessage());
        } else {
            // En el entorno prod daremos un mensaje genérico
            body.put(MESSAGE, "Se ha producido un error en la solicitud");
        }

        return body;
    }

    /**
     * Maneja las ApiException.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex, WebRequest request) {
        HttpStatus status = getStatusForCode(ex.getCode());
        return new ResponseEntity<>(
                buildResponse(status, ex, request.getDescription(false)), status);
    }

    /**
     * Maneja errores de validación de @Valid en los controladores.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        ApiException apiEx = new ApiException(ErrorCodes.BAD_PARAMS, ErrorCodes.BAD_PARAMS_MSG);
        Map<String, Object> body = buildResponse(HttpStatus.BAD_REQUEST, apiEx, request.getDescription(false));

        if (!"prod".equalsIgnoreCase(activeProfile)) {
            Map<String, String> fieldErrors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(
                            fe -> fe.getField(),
                            fe -> fe.getDefaultMessage(),
                            (existing, replacement) -> existing // evitar duplicados
                    ));
            body.put(ERRORS, fieldErrors);
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Cualquier excepción no controlada se transforma en ApiException.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error("Excepción inesperada capturada", ex);
        ApiException apiEx = new ApiException(ErrorCodes.BAD_PARAMS, ErrorCodes.BAD_PARAMS_MSG);
        return new ResponseEntity<>(
                buildResponse(HttpStatus.BAD_REQUEST, apiEx, request.getDescription(false)),
                HttpStatus.BAD_REQUEST
        );
    }

    private HttpStatus getStatusForCode(String code) {
        if (code.startsWith("AUTH_")) return HttpStatus.UNAUTHORIZED;
        if (code.equals(ErrorCodes.FEATURE_EXISTS)) return HttpStatus.CONFLICT;
        if (code.startsWith("USER_")) return HttpStatus.BAD_REQUEST;
        if (code.startsWith("FF_")) return HttpStatus.NOT_FOUND;
        if (code.startsWith("PARAMS_")) return HttpStatus.BAD_REQUEST;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
