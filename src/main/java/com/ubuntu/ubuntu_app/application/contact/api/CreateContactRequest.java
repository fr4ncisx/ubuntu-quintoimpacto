package com.ubuntu.ubuntu_app.application.contact.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateContactRequest(
        @NotNull
        @NotBlank
        @Size(max = 150, message = "El nombre es demasiado largo")
        @JsonProperty("full_name")
        String fullName,
        @NotNull
        @NotBlank
        @Email(message = "Formato de correo invalido")
        @Size(max = 150, message = "El correo es demasiado largo")
        @JsonProperty("email")
        String email,
        @NotNull
        @NotBlank
        @Size(max = 25, message = "El telefono es demasiado largo")
        @Pattern(regexp = "\\+\\d+( \\d+)*", message = "El numero debe comenzar con + seguido de dígitos y puede contener espacios")
        @JsonProperty("phone")
        String phone,
        @NotNull
        @NotBlank
        @Size(max = 300, message = "El mensaje no debe superar los 300 caracteres")
        @JsonProperty("message")
        String message) {
}
