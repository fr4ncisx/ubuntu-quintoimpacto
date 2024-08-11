package com.ubuntu.ubuntu_app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    @JsonProperty("apellido_nombre")
    @NotNull
    @NotBlank
    @Size(max = 100, message = "El nombre es demasiado largo")    
    private String nombreCompleto;
    @NotNull
    @NotBlank
    @Email(message = "Formato de correo invalido")
    @JsonProperty("correo_electronico")
    private String email;
    @NotNull
    @NotBlank
    @JsonProperty("numero_telefono")
    private String telefono;
    @NotNull
    @NotBlank
    @Size(max = 300, message = "El mensaje no debe superar los 300 caracteres")
    @JsonProperty("mensaje_contacto")
    private String mensaje;
}
