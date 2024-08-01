package com.ubuntu.ubuntu_app.model.dto;

import java.time.LocalDate;

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
}
