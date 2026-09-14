package com.ubuntu.ubuntu_app.infrastructure.contact.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ubuntu.ubuntu_app.application.contact.api.CreateContactRequest;
import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

@Mapper(componentModel = "spring")
public interface ContactRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullName", source = "dto.fullName")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "phone", source = "dto.phone")
    @Mapping(target = "message", source = "dto.message")
    @Mapping(target = "reviewed", constant = "false")
    @Mapping(target = "microbusiness", source = "micro")
    @Mapping(target = "email", source = "dto.email")
    ContactRequestEntity toEntity(CreateContactRequest dto, MicrobusinessEntity micro);
}
