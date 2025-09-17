package com.bytescolab.featureflag.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la aplicación FeatureFlag API.
 * <p>
 * Esta clase define la configuración de la documentación de la API generada con
 * <a href="https://swagger.io/">Swagger</a> y expuesta a través de OpenAPI 3.
 * Incluye la definición del título, versión, descripción y los esquemas de seguridad
 * necesarios para interactuar con los endpoints protegidos.
 * </p>
 *
 * <p>
 * Se configura un esquema de autenticación tipo <b>Bearer</b> con formato JWT,
 * lo que permite a los clientes autenticar las peticiones mediante un token válido
 * en la cabecera <i>Authorization</i>.
 * </p>
 *
 * <p><b>Ejemplo de uso de autenticación:</b></p>
 * <pre>
 * Authorization: Bearer &lt;token-jwt&gt;
 * </pre>
 *
 * @author Bytes
 * @see OpenAPI
 * @see SecurityScheme
 */
@Configuration
public class SwaggerConfig {

    /**
     * Define la configuración personalizada de OpenAPI para la aplicación.
     *
     * @return un objeto {@link OpenAPI} con:
     *         <ul>
     *             <li>Información general de la API (título, versión y descripción).</li>
     *             <li>Esquema de seguridad basado en Bearer Authentication con JWT.</li>
     *         </ul>
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FeatureFlag API")
                        .version("1.0.0")
                        .description("API para la gestión dinámica de features")
                )
                //Añadimos la seguridad, por lo que se necesitará del token correspondiente según rol para los endpoints
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}