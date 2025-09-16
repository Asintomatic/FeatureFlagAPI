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
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

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
        //Se recoge el token de las cabeceras
        final String authHeader = request.getHeader("Authorization");
        log.info("Recogemos el token {}", authHeader );

        //Si no tenemos header o no corresponde con Bearer, no se continua.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            log.warn("Token nulo o  formato inválido.");
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            String username = jwtUtils.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //recogemos el usuario de bbdd
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //si token válido, metemos en contexto
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
            }
            SecurityContextHolder.clearContext();
            throw e;  // Re-lanza la ApiException
        }

        filterChain.doFilter(request, response);
    }
}