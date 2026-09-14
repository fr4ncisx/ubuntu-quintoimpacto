package com.ubuntu.ubuntu_app.infrastructure.province.adapter.mapper;

import org.mapstruct.Mapper;

import com.ubuntu.ubuntu_app.application.province.api.ProvinceResponse;
import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;

@Mapper(componentModel = "spring")
public interface ProvinceMapper {

    ProvinceResponse toDto(ProvinceEntity entity);
}
