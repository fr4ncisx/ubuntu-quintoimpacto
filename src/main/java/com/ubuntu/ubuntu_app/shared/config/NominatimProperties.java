package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "nominatim")
public record NominatimProperties(
        @NotBlank String search,
        @NotBlank String reverse) {
}
