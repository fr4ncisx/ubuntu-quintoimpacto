package com.ubuntu.ubuntu_app.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @NotBlank
    @NotNull
    private String nombre;
    @NotBlank
    @NotNull
    private String apellido;
    @NotBlank
    @NotNull
    private String telefono;
    @NotNull
    @NotBlank
    private String imagen;
    @NotNull
    private boolean suscribed;
}
