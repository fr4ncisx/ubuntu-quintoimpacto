package com.ubuntu.ubuntu_app.infrastructure.category.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.category.port.out.CategoryRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;
import com.ubuntu.ubuntu_app.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryJpaAdapter implements CategoryRepositoryPort {

    private final CategoryRepository repository;

    @Override
    public CategoryEntity save(CategoryEntity category) {
        return repository.save(category);
    }

    @Override
    public List<CategoryEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CategoryEntity> findByName(String name) {
        return repository.findByName(name);
    }
}
