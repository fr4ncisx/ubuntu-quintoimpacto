package com.ubuntu.ubuntu_app.application.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank @NotNull String firstName,
        @NotBlank @NotNull String lastName,
        @NotBlank @NotNull String phone,
        @NotNull @NotBlank String image,
        @NotNull boolean subscribed) {
}
