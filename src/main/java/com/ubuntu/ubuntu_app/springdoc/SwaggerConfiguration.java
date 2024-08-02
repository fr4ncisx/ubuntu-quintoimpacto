package com.ubuntu.ubuntu_app.springdoc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .components(new Components()
                                                .addSecuritySchemes("bearer-key",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")))
                                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
        }
        @Bean
        public GroupedOpenApi publicApi() {
            return GroupedOpenApi.builder()
                    .group("public")
                    .pathsToMatch("/**")
                    .build();
        }
}
