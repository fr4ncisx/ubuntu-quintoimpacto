package com.ubuntu.ubuntu_app.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ubuntu.ubuntu_app.Repository.CategoryRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.CategoryDTO;
import com.ubuntu.ubuntu_app.model.entities.CategoryEntity;

import lombok.RequiredArgsConstructor;

@Lazy
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public ResponseEntity<?> createCategory(CategoryDTO categoryDTO) {
        repository.save(new CategoryEntity(null,categoryDTO.getNombre())); 
        var jsonResponse = ResponseMap.createResponse("Categoría creada exitosamente");      
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllCategories() {
        var categoryList = repository.findAll();
        if (!categoryList.isEmpty()) {
            var categoryDTO = categoryList.stream().map(c -> MapperConverter.generate().map(c, CategoryDTO.class)).toList();
            return new ResponseEntity<>(categoryDTO, HttpStatus.ACCEPTED);
        } else {
            throw new SqlEmptyResponse("No se encontraron categorías en la base de datos");
        }
    }
}
