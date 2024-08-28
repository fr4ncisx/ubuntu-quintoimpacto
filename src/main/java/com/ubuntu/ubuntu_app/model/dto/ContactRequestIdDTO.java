package com.ubuntu.ubuntu_app.model.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;

import com.ubuntu.ubuntu_app.model.entities.ContactRequestEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestIdDTO {
    private MicrobusinessNameDTO microemprendimiento;
    private Long id;
    private LocalDate fecha_solicitud;
    private String nombre;
    private String email;
    private String telefono;
    private String mensaje;
    private boolean gestionado;

    public ContactRequestIdDTO(ContactRequestEntity contactRequest) {
        ModelMapper modelMapper = new ModelMapper();
        this.microemprendimiento = modelMapper.map(contactRequest.getMicrobusiness(), MicrobusinessNameDTO.class);
        this.id = contactRequest.getId();
        this.fecha_solicitud = contactRequest.getDate();
        this.nombre = contactRequest.getFullName();
        this.email = contactRequest.getEmail();
        this.telefono = contactRequest.getPhone();
        this.mensaje = contactRequest.getMessage();
        this.gestionado = contactRequest.isReviewed();
    }
}
