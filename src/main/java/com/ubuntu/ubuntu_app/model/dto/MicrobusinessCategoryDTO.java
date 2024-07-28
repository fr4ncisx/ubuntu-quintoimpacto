package com.ubuntu.ubuntu_app.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicrobusinessCategoryDTO {
    
    private String nombre;
    
    private String descripcion;
    
    private String masInformacion;
    
    private String pais;
    
    private String provincia;
    
    private String ciudad;

    private String subcategoria;
    
    private List<ImageDTO> imagenes;
}
