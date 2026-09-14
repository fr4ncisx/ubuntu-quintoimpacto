package com.ubuntu.ubuntu_app.application.category.port.out;

import java.util.List;
import java.util.Optional;

import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;

public interface CategoryRepositoryPort {

    CategoryEntity save(CategoryEntity category);

    List<CategoryEntity> findAll();

    Optional<CategoryEntity> findByName(String name);
}
