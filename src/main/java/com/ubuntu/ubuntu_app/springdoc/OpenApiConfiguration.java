package com.ubuntu.ubuntu_app.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@OpenAPIDefinition(servers = {
        @Server(url = "https://ubuntu.koyeb.app/", description = "Deploy server"),
        @Server(url = "http://localhost:8080/", description = "Local server")
})
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ubuntu API")
                        .version("1.0.0")
                        .description("Social enterprise platform: microbusinesses, publications, categories, chatbot"))
                .addTagsItem(new Tag().name("Users").description("User management"))
                .addTagsItem(new Tag().name("Countries & Provinces").description("Geographic data"))
                .addTagsItem(new Tag().name("Microbusinesses").description("Microbusiness CRUD and search"))
                .addTagsItem(new Tag().name("Publications").description("Publication CRUD and statistics"))
                .addTagsItem(new Tag().name("Contact Requests").description("Contact request workflow"))
                .addTagsItem(new Tag().name("Categories").description("Category catalog"))
                .addTagsItem(new Tag().name("Chatbot").description("Chatbot Q&A"))
                .addTagsItem(new Tag().name("Media").description("Image upload via Cloudinary"))
                .addTagsItem(new Tag().name("Auth").description("Authentication and token rotation"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}
