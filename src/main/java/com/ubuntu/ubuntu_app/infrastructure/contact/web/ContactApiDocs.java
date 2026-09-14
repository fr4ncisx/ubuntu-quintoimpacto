package com.ubuntu.ubuntu_app.infrastructure.contact.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class ContactApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "createContactRequest",
            new EndpointDoc("Create contact request", "Creates a contact request for a microbusiness",
                    "Contact Requests"),
            "listByReviewStatus",
            new EndpointDoc("List requests by review status", "Paginated requests filtered by status",
                    "Contact Requests"),
            "findContactRequest",
            new EndpointDoc("Find request by id", "Returns a contact request", "Contact Requests"),
            "markAsReviewed",
            new EndpointDoc("Mark request as reviewed", "Flags a request as managed", "Contact Requests"),
            "statistics",
            new EndpointDoc("Current month statistics", "Request count grouped by category",
                    "Contact Requests"));

    @Bean
    public GroupedOpenApi contactApi() {
        return GroupedOpenApi.builder().group("contact").pathsToMatch("/api/v1/contact-requests/**").build();
    }

    @Bean
    public GlobalOperationCustomizer contactOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(ContactRequestV1Controller.class)) {
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
