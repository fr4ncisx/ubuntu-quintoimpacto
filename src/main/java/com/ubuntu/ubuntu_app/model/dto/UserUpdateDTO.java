package com.ubuntu.ubuntu_app.model.dto;

import com.ubuntu.ubuntu_app.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private UserRole rol;
    private boolean activo;
}
