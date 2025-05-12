package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.CategoryRepository;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.model.dto.CategoryDTO;
import com.ubuntu.ubuntu_app.model.entities.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories() {
        CategoryEntity mockEntity = new CategoryEntity(1L, "AGRICULTURA");
        CategoryDTO mockDto = new CategoryDTO("AGRICULTURA");
        List<CategoryEntity> mockEntityList = List.of(mockEntity);

        Mockito.when(categoryRepository.findAll()).thenReturn(mockEntityList);
        Mockito.when(modelMapper.map(mockEntity, CategoryDTO.class)).thenReturn(mockDto);


        ResponseEntity<?> response = categoryService.getAllCategories();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertDoesNotThrow(() -> categoryService.getAllCategories());
        Mockito.verify(categoryRepository, times(2)).findAll();
        Mockito.verify(modelMapper, times(2)).map(mockEntity, CategoryDTO.class);
    }

    @Test
    void getAllCategoriesEmpty() {

        Mockito.when(categoryRepository.findAll()).thenReturn(Collections.emptyList());


        assertThrows(SqlEmptyResponse.class, () -> categoryService.getAllCategories());

        Mockito.verify(categoryRepository).findAll();
    }
}