package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "email")
public record EmailProperties(
        @NotBlank String address,
        @NotBlank String password) {
}
