package com.ubuntu.ubuntu_app.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestDTO {
    @NotNull
    @NotBlank
    private String nombreCompleto;
    @NotNull
    @NotBlank
    @Email(message = "Formato de correo invalido")
    private String email;
    @NotNull
    @NotBlank
    private String telefono;
    @NotNull
    @NotBlank
    @Size(max = 300, message = "El mensaje no debe superar los 300 caracteres")
    private String mensaje;
}
