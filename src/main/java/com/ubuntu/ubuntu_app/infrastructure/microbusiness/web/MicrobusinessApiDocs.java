package com.ubuntu.ubuntu_app.infrastructure.microbusiness.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class MicrobusinessApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.ofEntries(
            Map.entry("create",
                    new EndpointDoc("Create microbusiness", "Creates a new microbusiness", "Microbusinesses")),
            Map.entry("update",
                    new EndpointDoc("Update microbusiness by id", "Updates microbusiness details",
                            "Microbusinesses")),
            Map.entry("delete",
                    new EndpointDoc("Delete microbusiness by id", "Deletes a microbusiness", "Microbusinesses")),
            Map.entry("searchByName",
                    new EndpointDoc("Search by name", "Full-text search of microbusinesses", "Microbusinesses")),
            Map.entry("findByCategory",
                    new EndpointDoc("List by category", "Returns microbusinesses of a category",
                            "Microbusinesses")),
            Map.entry("setVisibility",
                    new EndpointDoc("Activate or hide microbusiness", "Toggles microbusiness visibility",
                            "Microbusinesses")),
            Map.entry("findAll",
                    new EndpointDoc("List all active microbusinesses", "Paginated active microbusinesses",
                            "Microbusinesses")),
            Map.entry("findByStatus",
                    new EndpointDoc("List by active status", "Paginated microbusinesses by status",
                            "Microbusinesses")),
            Map.entry("statisticsByMonth",
                    new EndpointDoc("Current month statistics", "Microbusiness count for the current month",
                            "Microbusinesses")),
            Map.entry("statisticsByCategory",
                    new EndpointDoc("Monthly statistics by category", "Microbusiness count grouped by category",
                            "Microbusinesses")),
            Map.entry("findNear",
                    new EndpointDoc("Nearby microbusinesses by coordinates", "Geospatial search around a point",
                            "Microbusinesses")));

    @Bean
    public GroupedOpenApi microbusinessApi() {
        return GroupedOpenApi.builder().group("microbusiness").pathsToMatch("/api/v1/microbusiness/**").build();
    }

    @Bean
    public GlobalOperationCustomizer microbusinessOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(MicrobusinessV1Controller.class)) {
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
