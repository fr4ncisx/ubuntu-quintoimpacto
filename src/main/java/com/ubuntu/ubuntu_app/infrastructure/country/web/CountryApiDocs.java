package com.ubuntu.ubuntu_app.infrastructure.country.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class CountryApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "findAllCountries",
            new EndpointDoc("List all countries", "Returns all countries", "Countries & Provinces"),
            "findProvincesByCountry",
            new EndpointDoc("List provinces by country", "Returns provinces filtered by country name",
                    "Countries & Provinces"));

    @Bean
    public GroupedOpenApi geoApi() {
        return GroupedOpenApi.builder().group("geo").pathsToMatch("/api/v1/countries", "/api/v1/provinces")
                .build();
    }

    @Bean
    public GlobalOperationCustomizer countryOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(CountryV1Controller.class)) {
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
