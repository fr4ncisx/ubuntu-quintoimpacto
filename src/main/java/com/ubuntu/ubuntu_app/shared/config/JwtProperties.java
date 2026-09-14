package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        @Valid Secret secret) {

    public record Secret(
            @NotBlank String key) {
    }
}
