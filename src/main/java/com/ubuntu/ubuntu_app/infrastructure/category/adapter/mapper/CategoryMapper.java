package com.ubuntu.ubuntu_app.infrastructure.category.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ubuntu.ubuntu_app.application.category.api.CategoryRequest;
import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;
import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toDto(CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequest dto);
}
