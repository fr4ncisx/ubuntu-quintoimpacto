package com.ubuntu.ubuntu_app.infrastructure.country.adapter.mapper;

import org.mapstruct.Mapper;

import com.ubuntu.ubuntu_app.application.country.api.CountryResponse;
import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryResponse toDto(CountryEntity entity);
}
