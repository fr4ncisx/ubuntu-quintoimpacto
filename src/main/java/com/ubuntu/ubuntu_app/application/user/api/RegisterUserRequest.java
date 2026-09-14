package com.ubuntu.ubuntu_app.application.user.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
        @NotBlank(message = "Tiene que haber un nombre") String firstName,
        @NotBlank(message = "Tiene que haber un apellido") String lastName,
        @NotBlank @Email(message = "Email invalido") String email) {
}
