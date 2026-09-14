package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

@Validated
@ConfigurationProperties(prefix = "chatbot")
public record ChatbotProperties(
        @Valid Similarity similarity) {

    public record Similarity(
            double threshold) {
    }
}
