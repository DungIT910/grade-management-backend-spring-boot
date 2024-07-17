package com.boolfly.grademanagementrestful.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI documentation.
 * <p>
 * This class configures the OpenAPI documentation for the Grade Management Restful API.
 *
 * @see OpenAPI
 * @see Info
 *
 * @author Bao Le
 */
@Configuration
public class OpenAPIConfig {

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
                        .version("v1.0")
                );
    }
}
