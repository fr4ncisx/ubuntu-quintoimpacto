package com.ubuntu.ubuntu_app.infrastructure.publication.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class PublicationApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.ofEntries(
            Map.entry("createPublication",
                    new EndpointDoc("Create publication", "Creates a new publication", "Publications")),
            Map.entry("findPublicationById",
                    new EndpointDoc("Find publication by id", "Returns a publication", "Publications")),
            Map.entry("updatePublication",
                    new EndpointDoc("Update publication by id", "Updates publication details", "Publications")),
            Map.entry("deletePublication",
                    new EndpointDoc("Delete publication by id", "Deletes a publication", "Publications")),
            Map.entry("findAllPublications",
                    new EndpointDoc("List active or inactive publications", "Paginated publications by status",
                            "Publications")),
            Map.entry("searchPublications",
                    new EndpointDoc("Search publications by title", "Full-text title search", "Publications")),
            Map.entry("newVisualization",
                    new EndpointDoc("Register view", "Increments the publication view counter", "Publications")),
            Map.entry("setVisibility",
                    new EndpointDoc("Activate or hide publication", "Toggles publication visibility",
                            "Publications")),
            Map.entry("publicationStatistics",
                    new EndpointDoc("Top publications of the month", "Most viewed publications", "Publications")));

    @Bean
    public GroupedOpenApi publicationsApi() {
        return GroupedOpenApi.builder().group("publications").pathsToMatch("/api/v1/publications/**").build();
    }

    @Bean
    public GlobalOperationCustomizer publicationOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(PublicationV1Controller.class)) {
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
