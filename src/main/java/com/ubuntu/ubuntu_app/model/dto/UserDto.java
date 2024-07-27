package com.ubuntu.ubuntu_app.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "Tiene que haber un nombre")
    private String nombre;
    @NotBlank(message = "Tiene que haber un apellido")
    private String apellido;
    @NotBlank @Email(message = "Email invalido")
    private String email;   

}
