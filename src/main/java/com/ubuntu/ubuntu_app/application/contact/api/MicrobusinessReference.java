package com.ubuntu.ubuntu_app.application.contact.api;

import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

public record MicrobusinessReference(
        Long id,
        String name) {

    public MicrobusinessReference(MicrobusinessEntity microbusiness) {
        this(microbusiness.getId(), microbusiness.getName());
    }
}
