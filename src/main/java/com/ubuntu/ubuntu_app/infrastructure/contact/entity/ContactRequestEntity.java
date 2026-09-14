package com.ubuntu.ubuntu_app.infrastructure.contact.entity;

import java.time.LocalDate;
import java.util.Objects;

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

    public ContactRequestEntity() {
    }

    public ContactRequestEntity(Long id, String fullName, String email, LocalDate date, String phone,
            String message, boolean reviewed, MicrobusinessEntity microbusiness) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.date = date;
        this.phone = phone;
        this.message = message;
        this.reviewed = reviewed;
        this.microbusiness = microbusiness;
    }

    public ContactRequestEntity(CreateContactRequest requestMessage, MicrobusinessEntity foundMicro) {
        this.fullName = requestMessage.fullName();
        this.email = requestMessage.email();
        this.date = LocalDate.now();
        this.phone = requestMessage.phone();
        this.message = requestMessage.message();
        this.reviewed = false;
        this.microbusiness = foundMicro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public MicrobusinessEntity getMicrobusiness() {
        return microbusiness;
    }

    public void setMicrobusiness(MicrobusinessEntity microbusiness) {
        this.microbusiness = microbusiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactRequestEntity that = (ContactRequestEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ContactRequestEntity{id=" + id + ", fullName='" + fullName + "', email='" + email + "', date="
                + date + ", reviewed=" + reviewed + "}";
    }
}
