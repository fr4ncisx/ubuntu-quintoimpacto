package com.ubuntu.ubuntu_app.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicrobusinessDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    @Size(max = 300)
    private String descripcion;
    @NotBlank
    @Size(max = 300)
    private String masInformacion;
    @NotBlank
    private String pais;
    @NotBlank
    private String provincia;
    @NotBlank
    private String ciudad;
    @NotNull
    private CategoryDTO categoria;
    private String subcategoria;
    @NotNull
    private List<ImageDTO> imagenes;
}
