package com.ubuntu.ubuntu_app.application.contact.api;

import java.time.LocalDate;

import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;

public record ContactRequestResponse(
        MicrobusinessReference microbusiness,
        Long id,
        LocalDate requestDate,
        String name,
        String email,
        String phone,
        String message,
        boolean reviewed) {

    public ContactRequestResponse(ContactRequestEntity contactRequest) {
        this(new MicrobusinessReference(contactRequest.getMicrobusiness()),
                contactRequest.getId(),
                contactRequest.getDate(),
                contactRequest.getFullName(),
                contactRequest.getEmail(),
                contactRequest.getPhone(),
                contactRequest.getMessage(),
                contactRequest.isReviewed());
    }
}
