package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        @Valid Google google,
        @Valid Jwt jwt) {

    public record Google(
            @NotBlank String postLoginRedirect) {
    }

    public record Jwt(
            @NotBlank String cookieName,
            @NotBlank String refreshCookieName) {
    }
}
