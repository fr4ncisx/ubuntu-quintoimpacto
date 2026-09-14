package com.ubuntu.ubuntu_app.infrastructure.user.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class UserApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "createUser", new EndpointDoc("Create user", "Registers a new user account", "Users"),
            "getAllUsers", new EndpointDoc("List all users", "Returns paginated list of users", "Users"),
            "update", new EndpointDoc("Update user by id", "Updates user details", "Users"),
            "deactivate", new EndpointDoc("Deactivate user by id", "Soft-deletes a user", "Users"));

    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder().group("users").pathsToMatch("/api/v1/users/**").build();
    }

    @Bean
    public GlobalOperationCustomizer userOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(UserV1Controller.class)) {
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
