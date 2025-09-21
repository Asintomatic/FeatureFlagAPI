package com.bytescolab.featureflag.config.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/**
 * {@link AccessDeniedHandler} personalizado para gestionar errores de autorización en la API.
 *
 * <p>Se ejecuta automáticamente cuando un usuario autenticado intenta acceder a un recurso
 * para el cual no tiene permisos suficientes (error HTTP 403 - Forbidden).</p>
 *
 * <h2>Características:</h2>
 * <ul>
 *   <li>Devuelve la respuesta en formato JSON, en lugar de redirigir a una página de error.</li>
 *   <li>Incluye información útil en la respuesta, como la marca de tiempo, el código de estado,
 *       el tipo de error, un mensaje descriptivo y la ruta solicitada.</li>
 *   <li>Se registra como un {@link Component} de Spring, lo que permite su inyección y
 *       uso automático dentro de la configuración de seguridad.</li>
 * </ul>
 *
 * @author Bytes
 * @see org.springframework.security.web.access.AccessDeniedHandler
 * @see org.springframework.security.access.AccessDeniedException
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Maneja el error de acceso denegado generando una respuesta JSON con detalles del error.
     *
     * @param req la solicitud HTTP que provocó el error.
     * @param res la respuesta HTTP que se enviará al cliente.
     * @param ex la excepción que representa el intento de acceso no autorizado.
     * @throws IOException si ocurre un error al escribir en la salida de la respuesta.
     */
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException {
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setContentType("application/json");

        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden",
                "message", "You do not have permission to access this resource",
                "path", req.getRequestURI()
        );

        new ObjectMapper().writeValue(res.getOutputStream(), body);
    }
}
