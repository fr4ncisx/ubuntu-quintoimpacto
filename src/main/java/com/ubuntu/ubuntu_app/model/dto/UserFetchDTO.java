package com.ubuntu.ubuntu_app.model.dto;

import com.ubuntu.ubuntu_app.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFetchDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;
    private UserRole rol;
    private String telefono;
}
