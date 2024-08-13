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
    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "nombre")
    private String nombre;
    @JsonProperty(value = "descripcion")
    private String descripcion;
    @JsonProperty(value = "masInformacion")
    private String masInformacion;
    @JsonProperty(value = "pais")
    private String pais;
    @JsonProperty(value = "provincia")
    private String provincia;
    @JsonProperty(value = "ciudad")
    private String ciudad;
    @JsonProperty(value = "categoria")
    private CategoryDTO categoria;
    @JsonProperty(value = "subcategoria")
    private String subcategoria;
    @JsonProperty(value = "fecha_creacion")
    private LocalDate fecha;
    @JsonProperty(value = "imagenes")
    private List<ImageDTO> imagenes;
}
