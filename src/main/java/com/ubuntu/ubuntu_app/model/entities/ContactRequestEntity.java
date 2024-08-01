package com.ubuntu.ubuntu_app.model.entities;

import java.time.LocalDate;

import com.ubuntu.ubuntu_app.model.dto.ContactRequestDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Contacto")
public class ContactRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "apellido_nombre")
    private String fullName;
    private String email;
    @Column(name = "fecha_creacion")
    private LocalDate date;
    @Column(name = "telefono")
    private String phone;
    @Column(name = "mensaje")
    private String message;
    @Column(name = "gestionado")
    private boolean reviewed;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_micro")
    private MicrobusinessEntity microbusiness;
    
    public ContactRequestEntity(ContactRequestDTO requestMessage, MicrobusinessEntity foundMicro) {
        this.fullName = requestMessage.getNombreCompleto();
        this.email = requestMessage.getEmail();
        this.date = LocalDate.now();
        this.phone = requestMessage.getTelefono();
        this.message = requestMessage.getMensaje();
        this.reviewed = false;
        this.microbusiness = foundMicro;
    }

    
}
