package com.ubuntu.ubuntu_app.infrastructure.publication.adapter.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ubuntu.ubuntu_app.application.publication.api.CreatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.PublicationResponse;
import com.ubuntu.ubuntu_app.application.publication.api.UpdatePublicationRequest;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationEntity;

@Mapper(componentModel = "spring")
public interface PublicationMapper {

    PublicationResponse toDto(PublicationEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "images", source = "images")
    PublicationEntity toEntity(CreatePublicationRequest dto, List<ImageEntity> images);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "images", source = "images")
    void updateEntity(UpdatePublicationRequest dto, List<ImageEntity> images,
            @MappingTarget PublicationEntity entity);
}
