package com.ubuntu.ubuntu_app.model.dto;

import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicrobusinessNameDTO {

    private Long id;
    private String nombre;

    public MicrobusinessNameDTO(MicrobusinessEntity microbusiness) {
        this.id = microbusiness.getId();
        this.nombre = microbusiness.getNombre();
    }
}
