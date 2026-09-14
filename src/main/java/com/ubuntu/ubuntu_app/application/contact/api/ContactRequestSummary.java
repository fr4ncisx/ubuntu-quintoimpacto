package com.ubuntu.ubuntu_app.application.contact.api;

import java.time.LocalDate;

import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;

public record ContactRequestSummary(
        MicrobusinessReference microbusiness,
        Long id,
        LocalDate requestDate,
        String name,
        String email,
        String phone,
        String message) {

    public ContactRequestSummary(MicrobusinessReference microbusinessReference,
            ContactRequestEntity contactEntity) {
        this(microbusinessReference, contactEntity.getId(), contactEntity.getDate(),
                contactEntity.getFullName(), contactEntity.getEmail(), contactEntity.getPhone(),
                contactEntity.getMessage());
    }
}
