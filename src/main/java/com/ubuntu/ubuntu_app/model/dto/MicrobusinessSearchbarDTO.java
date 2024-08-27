package com.ubuntu.ubuntu_app.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "nombre", "descripcion", "masInformacion", "pais", "provincia", "ciudad", "categoria",
        "subcategoria", "imagenes" })
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
    private List<ImageDTO> imagenes;
}
