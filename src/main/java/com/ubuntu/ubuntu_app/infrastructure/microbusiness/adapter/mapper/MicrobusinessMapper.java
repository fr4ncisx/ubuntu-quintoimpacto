package com.ubuntu.ubuntu_app.infrastructure.microbusiness.adapter.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ubuntu.ubuntu_app.application.microbusiness.api.CreateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessCategorySummary;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessSummary;
import com.ubuntu.ubuntu_app.application.microbusiness.api.UpdateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

@Mapper(componentModel = "spring")
public interface MicrobusinessMapper {

    MicrobusinessSummary toSearchbarDto(MicrobusinessEntity entity);

    MicrobusinessCategorySummary toCategoryDto(MicrobusinessEntity entity);

    CreateMicrobusinessRequest toCreateRequest(MicrobusinessEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "moreInfo", source = "request.moreInfo")
    @Mapping(target = "country", source = "request.country")
    @Mapping(target = "province", source = "request.province")
    @Mapping(target = "city", source = "request.city")
    @Mapping(target = "subcategory", source = "request.subcategory")
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "mailed", constant = "false")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "images", source = "images")
    MicrobusinessEntity toEntity(CreateMicrobusinessRequest request, CategoryEntity category, List<ImageEntity> images);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "moreInfo", source = "request.moreInfo")
    @Mapping(target = "country", source = "request.country")
    @Mapping(target = "province", source = "request.province")
    @Mapping(target = "city", source = "request.city")
    @Mapping(target = "subcategory", source = "request.subcategory")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "mailed", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "images", source = "images")
    void updateEntity(UpdateMicrobusinessRequest request, CategoryEntity category, List<ImageEntity> images,
            @MappingTarget MicrobusinessEntity entity);
}
