package com.bytescolab.featureflag.security.jwt;

import com.bytescolab.featureflag.exception.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Filtro de seguridad para la validación de tokens JWT en cada petición HTTP.
 * <p>
 * Extiende {@link OncePerRequestFilter} para garantizar que la validación se ejecute una vez
 * por solicitud. Este filtro intercepta las peticiones entrantes, extrae el token JWT del header
 * {@code Authorization}, y valida su autenticidad y vigencia.
 * </p>
 *
 * <p>Comportamiento principal:</p>
 * <ul>
 *   <li>Excluye rutas que comiencen con <b>/api/auth/</b> para permitir el acceso sin autenticación.</li>
 *   <li>Valida el formato del token en la cabecera ({@code Bearer <token>}).</li>
 *   <li>Extrae el nombre de usuario del token y lo carga desde la base de datos
 *   usando {@link UserDetailsService}.</li>
 *   <li>Si el token es válido, crea un {@link UsernamePasswordAuthenticationToken} y lo
 *   almacena en el {@link SecurityContextHolder} para autenticar al usuario en el contexto de Spring Security.</li>
 *   <li>Maneja errores de validación del token lanzando {@link ApiException} con códigos definidos en {@link ErrorCodes}.</li>
 * </ul>
 *
 * <p>En caso de excepción relacionada con el token, limpia el {@link SecurityContextHolder}
 * y vuelve a lanzar la excepción para que otros manejadores (por ejemplo,
 * {@code GlobalExceptionHandler}) se encarguen de procesarla.</p>
 *
 * @author Bytes
 * @see JwtUtils
 * @see UserDetailsService
 * @see ApiException
 * @see ErrorCodes
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * Intercepta cada petición HTTP para validar si contiene un token JWT válido.
     *
     * @param request  la solicitud HTTP entrante
     * @param response la respuesta HTTP asociada
     * @param filterChain la cadena de filtros de Spring Security
     * @throws ServletException si ocurre un error en la cadena de filtros
     * @throws IOException si ocurre un error de E/S durante la ejecución del filtro
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String uri = request.getRequestURI();
        if (uri.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        log.info("Recogemos el token {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            log.warn("Token nulo o formato inválido.");
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            String username = jwtUtils.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    log.info("Usuario autenticado correctamente: {}", username);
                    log.debug("Rol del usuario: {}", userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.error("Token no válido para usuario {}", username);
                }
            }

        } catch (ApiException e) {
            switch (e.getCode()) {
                case ErrorCodes.TOKEN_EXPIRADO -> log.error("Token expirado: {}", e.getMessage());
                case ErrorCodes.TOKEN_MALFORMADO -> log.error("Token mal formado: {}", e.getMessage());
                case ErrorCodes.TOKEN_INVALIDO -> log.error("Error al validar el token JWT: {}", e.getMessage());
                default -> log.error("Error JWT desconocido [{}]: {}", e.getCode(), e.getMessage());
            }
            SecurityContextHolder.clearContext();
            throw e;
        }

        filterChain.doFilter(request, response);
    }
}