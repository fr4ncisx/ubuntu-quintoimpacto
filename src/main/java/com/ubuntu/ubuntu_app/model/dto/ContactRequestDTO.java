package com.ubuntu.ubuntu_app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Size(max = 150, message = "El nombre es demasiado largo")
    @JsonProperty("apellido_nombre")
    private String nombreCompleto;
    @NotNull
    @NotBlank
    @Email(message = "Formato de correo invalido")
    @Size(max = 150, message = "El correo es demasiado largo")
    @JsonProperty("correo_electronico")
    private String email;
    @NotNull
    @NotBlank
    @Size(max = 25, message = "El telefono es demasiado largo")
    @Pattern(regexp = "\\+\\d+( \\d+)*", message = "El numero debe comenzar con + seguido de dígitos y puede contener espacios")
    @JsonProperty("numero_telefono")
    private String telefono;
    @NotNull
    @NotBlank
    @Size(max = 300, message = "El mensaje no debe superar los 300 caracteres")
    @JsonProperty("mensaje_contacto")
    private String mensaje;
}
