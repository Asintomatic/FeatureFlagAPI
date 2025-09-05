package com.bytescolab.featureflag.security.jwt;

import com.bytescolab.featureflag.service.user.UserDetailsService;
import com.bytescolab.featureflag.exception.TokenExpiradoException;
import com.bytescolab.featureflag.exception.TokenInvalidoException;
import com.bytescolab.featureflag.exception.TokenMalFormadoException;
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

            //Comprobamos que el username está dentro del token, si no está autenticado
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

        } catch (TokenExpiradoException e) {
            log.error("Token expirado: {}", e.getMessage());
            throw new TokenExpiradoException("El token ha expirado");
        } catch (TokenMalFormadoException e) {
            log.error("Token mal formado: {}", e.getMessage());
            throw new TokenMalFormadoException("El token está mal formado");
        } catch (TokenInvalidoException e) {
            log.error("Error al validar el token JWT: {}", e.getMessage());
            throw new TokenInvalidoException("El token no ha podido ser validado.");
        }

        filterChain.doFilter(request, response);
    }
}