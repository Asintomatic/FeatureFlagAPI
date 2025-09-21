package com.bytescolab.featureflag.config.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
/**
 * {@link AuthenticationEntryPoint} personalizado para gestionar errores de autenticación en la API.
 *
 * <p>Se ejecuta automáticamente cuando un usuario intenta acceder a un recurso protegido
 * sin estar autenticado o cuando el token JWT no se proporciona.</p>
 *
 * <h2>Características:</h2>
 * <ul>
 *   <li>Devuelve una respuesta JSON clara en lugar de redirigir a una página de login.</li>
 *   <li>Incluye información útil: marca de tiempo, estado HTTP, tipo de error, mensaje descriptivo
 *       y la ruta solicitada.</li>
 *   <li>Se registra como un {@link Component}, por lo que Spring lo utiliza automáticamente
 *       como manejador global de errores de autenticación (HTTP 401 - Unauthorized).</li>
 * </ul>
 *
 * @author Bytes
 * @see org.springframework.security.web.AuthenticationEntryPoint
 * @see org.springframework.security.core.AuthenticationException
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Maneja errores de autenticación devolviendo una respuesta JSON con detalles del error.
     *
     * @param req la solicitud HTTP que provocó el error.
     * @param res la respuesta HTTP enviada al cliente.
     * @param ex la excepción lanzada cuando la autenticación falla.
     * @throws IOException si ocurre un error al escribir en la salida de la respuesta.
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType("application/json");

        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.UNAUTHORIZED.value(),
                "error", "Unauthorized",
                "message", "Authentication is required to access this resource",
                "path", req.getRequestURI()
        );

        new ObjectMapper().writeValue(res.getOutputStream(), body);
    }
}
