package com.ubuntu.ubuntu_app.infrastructure.media.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ubuntu.ubuntu_app.application.media.api.ImageReference;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "id", ignore = true)
    ImageEntity toEntity(ImageReference dto);
}
