package com.ubuntu.ubuntu_app.infrastructure.contact.entity;

import java.time.LocalDate;

import com.ubuntu.ubuntu_app.application.contact.api.CreateContactRequest;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

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
@Table(name = "contact_requests")
public class ContactRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    private String email;
    @Column(name = "created_at")
    private LocalDate date;
    @Column(name = "phone")
    private String phone;
    @Column(name = "message", length = 300)
    private String message;
    @Column(name = "reviewed")
    private boolean reviewed;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "microbusiness_id")
    private MicrobusinessEntity microbusiness;
    
    public ContactRequestEntity(CreateContactRequest requestMessage, MicrobusinessEntity foundMicro) {
        this.fullName = requestMessage.fullName();
        this.email = requestMessage.email();
        this.date = LocalDate.now();
        this.phone = requestMessage.phone();
        this.message = requestMessage.message();
        this.reviewed = false;
        this.microbusiness = foundMicro;
    }

    
}
