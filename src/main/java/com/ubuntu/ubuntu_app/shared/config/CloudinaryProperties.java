package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "cloudinary")
public record CloudinaryProperties(
        @NotBlank String urlConfig) {
}
