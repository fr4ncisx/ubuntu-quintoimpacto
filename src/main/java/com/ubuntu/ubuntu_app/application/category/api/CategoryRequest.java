package com.ubuntu.ubuntu_app.application.category.api;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "El nombre no debe estar vacio") String name) {
}
