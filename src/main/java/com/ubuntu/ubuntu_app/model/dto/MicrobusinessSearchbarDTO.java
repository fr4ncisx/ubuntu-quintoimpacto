package com.ubuntu.ubuntu_app.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicrobusinessSearchbarDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String masInformacion;
    private String pais;
    private String provincia;
    private String ciudad;
    private CategoryDTO categoria;
    private String subcategoria;
    @JsonProperty("fecha_creacion")
    private LocalDate fecha;
    private List<ImageDTO> imagenes;
}
