package com.ubuntu.ubuntu_app.model.dto;

import java.time.LocalDate;

import com.ubuntu.ubuntu_app.model.entities.ContactRequestEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestStatusDTO {

    private MicrobusinessNameDTO microemprendimiento;
    private Long id;
    private LocalDate fecha_solicitud;
    private String nombre;
    private String email;
    private String telefono;
    private String mensaje;

    public ContactRequestStatusDTO(MicrobusinessNameDTO microbusinessNameDTO, ContactRequestEntity contactEntity) {
        this(microbusinessNameDTO, contactEntity.getId(), contactEntity.getDate(), contactEntity.getFullName(),
                contactEntity.getEmail(), contactEntity.getPhone(), contactEntity.getMessage());
    }
}
