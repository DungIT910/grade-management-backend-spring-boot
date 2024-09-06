package com.boolfly.grademanagementrestful.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for setting up OpenAPI documentation.
 * <p>
 * This class configures the OpenAPI documentation for the Grade Management Restful API.
 *
 * @author Bao Le
 * @see OpenAPI
 * @see Info
 */
@Configuration
public class OpenAPIConfig {
    private static final String BEARER_AUTH_KEY = "bearerAuth";

    public OpenAPIConfig(MappingJackson2HttpMessageConverter converter) {
        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }

    /**
     * Creates and configures an {@link OpenAPI} bean with the API information.
     *
     * @return an {@link OpenAPI} instance with the configured API information
     */
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Grade Management Restful API")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_KEY,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList(BEARER_AUTH_KEY)))
                .addServersItem(new Server()
                        .url("http://localhost:8080"));
    }
}
