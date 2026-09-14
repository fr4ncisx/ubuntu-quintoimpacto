package com.ubuntu.ubuntu_app.application.category.service;

import org.springframework.stereotype.Service;
import com.ubuntu.ubuntu_app.application.category.port.in.CategoryUseCase;
import com.ubuntu.ubuntu_app.application.category.port.out.CategoryRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.shared.api.ResponseMap;
import com.ubuntu.ubuntu_app.infrastructure.category.adapter.mapper.CategoryMapper;
import com.ubuntu.ubuntu_app.application.category.api.CategoryRequest;
import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CategoryService implements CategoryUseCase {

    private final CategoryRepositoryPort repository;
    private final CategoryMapper mapper;

    @Override
    public Map<String, String> create(CategoryRequest categoryDTO) {
        repository.save(mapper.toEntity(categoryDTO));
        return ResponseMap.createResponse("Categoría creada exitosamente");
    }

    @Override
    public List<CategoryResponse> findAll() {
        var categoryList = repository.findAll();
        if (categoryList.isEmpty()) {
            throw new SqlEmptyResponse("No se encontraron categorías en la base de datos");
        }
        return categoryList.stream().map(mapper::toDto).toList();
    }
}
