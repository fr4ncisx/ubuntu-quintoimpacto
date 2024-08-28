package com.ubuntu.ubuntu_app.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicrobusinessGeoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private double distance;
    private String masInformacion;
    private String pais;
    private String provincia;
    private String ciudad;
    private CategoryDTO categoria;
    private String subcategoria;
    private List<ImageDTO> imagenes;

    public MicrobusinessGeoDTO(MicrobusinessEntity micro, double distance) {
        ModelMapper modelMapper = new ModelMapper();
        this.id = micro.getId();
        this.nombre = micro.getNombre();
        this.descripcion = micro.getDescripcion();
        this.distance = distance;
        this.masInformacion = micro.getMasInformacion();
        this.pais = micro.getPais();
        this.provincia = micro.getProvincia();
        this.ciudad = micro.getCiudad();
        this.categoria = modelMapper.map(micro.getCategoria(), CategoryDTO.class);
        this.subcategoria = micro.getSubcategoria();
        this.imagenes = micro.getImagenes().stream().map(i -> modelMapper.map(i, ImageDTO.class))
                .collect(Collectors.toList());
    }
}
