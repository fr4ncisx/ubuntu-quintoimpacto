package com.ubuntu.ubuntu_app.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;

@Validated
@ConfigurationProperties(prefix = "token")
public record TokenProperties(
        @Min(1) int expiration,
        @Min(1) long refreshExpiration) {
}
