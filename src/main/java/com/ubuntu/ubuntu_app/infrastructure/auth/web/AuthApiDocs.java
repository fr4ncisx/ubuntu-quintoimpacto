package com.ubuntu.ubuntu_app.infrastructure.auth.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class AuthApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "login",
            new EndpointDoc("Login", "Validates the authenticated session", "Auth"),
            "logout",
            new EndpointDoc("Logout", "Revokes refresh token and clears cookies", "Auth"),
            "refresh",
            new EndpointDoc("Refresh tokens", "Rotates the refresh token and issues a new pair", "Auth"));

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("auth").pathsToMatch("/api/v1/auth/**").build();
    }

    @Bean
    public GlobalOperationCustomizer authOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(AuthV1Controller.class)) {
                return operation;
            }
            EndpointDoc doc = DOCS.get(handlerMethod.getMethod().getName());
            if (doc != null) {
                operation.setSummary(doc.summary());
                operation.setDescription(doc.description());
                operation.setTags(List.of(doc.tag()));
            }
            return operation;
        };
    }

    public Set<String> documentedMethods() {
        return DOCS.keySet();
    }
}
