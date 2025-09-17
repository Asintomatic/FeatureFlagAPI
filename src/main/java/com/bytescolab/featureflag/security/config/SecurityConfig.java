package com.bytescolab.featureflag.security.config;


import com.bytescolab.featureflag.security.handlers.RestAuthenticationEntryPoint;
import com.bytescolab.featureflag.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bytescolab.featureflag.security.handlers.RestAccessDeniedHandler;

/**
 * Configuración central de seguridad para la aplicación FeatureFlag API.
 *
 * <p>Define las reglas de seguridad de Spring Security, la estrategia de autenticación
 * y la integración con el filtro JWT para garantizar que todas las peticiones estén
 * protegidas adecuadamente según el rol del usuario.</p>
 *
 * <h2>Características principales:</h2>
 * <ul>
 *   <li>Habilita la seguridad web con {@link EnableWebSecurity} y la seguridad basada en métodos con {@link EnableMethodSecurity}.</li>
 *   <li>Configura un {@link PasswordEncoder} seguro basado en {@link BCryptPasswordEncoder}.</li>
 *   <li>Define endpoints públicos accesibles sin autenticación
 *       ({@code /api/auth/**}, {@code /swagger-ui/**}, {@code /v3/api-docs/**}, entre otros).</li>
 *   <li>Protege el resto de endpoints, requiriendo autenticación mediante JWT.</li>
 *   <li>Configura la política de sesión como {@link SessionCreationPolicy#STATELESS},
 *       evitando el uso de sesiones en servidor.</li>
 *   <li>Registra manejadores personalizados para excepciones de autenticación y autorización:
 *       {@link RestAuthenticationEntryPoint} para errores 401 y {@link RestAccessDeniedHandler} para errores 403.</li>
 *   <li>Integra el filtro {@link JwtFilter} antes de {@link UsernamePasswordAuthenticationFilter}
 *       para validar tokens JWT en cada petición.</li>
 * </ul>
 *
 * @author Bytes
 * @see JwtFilter
 * @see RestAuthenticationEntryPoint
 * @see RestAccessDeniedHandler
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final RestAccessDeniedHandler accessDeniedHandler;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Configura el encoder de contraseñas para la aplicación.
     * <p>Se utiliza {@link BCryptPasswordEncoder} por su robustez
     * frente a ataques de fuerza bruta.</p>
     *
     * @return el codificador de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * @param http instancia de {@link HttpSecurity} para personalizar la seguridad HTTP.
     * @return la configuración de seguridad construida como {@link DefaultSecurityFilterChain}.
     * @throws Exception si ocurre un error en la configuración de seguridad.
     */
    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/error" ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Expone el {@link AuthenticationManager} utilizado por Spring Security para la autenticación.
     *
     * @param config configuración de autenticación proporcionada por Spring Boot.
     * @return el {@link AuthenticationManager} configurado.
     * @throws Exception si ocurre un error durante la inicialización.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
