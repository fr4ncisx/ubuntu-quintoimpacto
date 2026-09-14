package com.ubuntu.ubuntu_app.infrastructure.category.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class CategoryApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "createCategory",
            new EndpointDoc("Create category", "Creates a new category", "Categories"),
            "findAllCategories",
            new EndpointDoc("List all categories", "Returns the full category catalog", "Categories"));

    @Bean
    public GroupedOpenApi categoriesApi() {
        return GroupedOpenApi.builder().group("categories").pathsToMatch("/api/v1/categories/**").build();
    }

    @Bean
    public GlobalOperationCustomizer categoryOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(CategoryV1Controller.class)) {
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
