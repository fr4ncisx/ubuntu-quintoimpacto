package com.ubuntu.ubuntu_app.infrastructure.media.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class MediaApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "uploadImages",
            new EndpointDoc("Upload images to Cloudinary", "Uploads one or more images", "Media"),
            "deleteImage",
            new EndpointDoc("Delete image from Cloudinary", "Deletes an image by public id", "Media"),
            "replaceImage",
            new EndpointDoc("Replace image in Cloudinary", "Replaces an image by public id", "Media"));

    @Bean
    public GroupedOpenApi mediaApi() {
        return GroupedOpenApi.builder().group("media").pathsToMatch("/api/v1/cloudinary/**").build();
    }

    @Bean
    public GlobalOperationCustomizer mediaOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(CloudinaryV1Controller.class)) {
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
