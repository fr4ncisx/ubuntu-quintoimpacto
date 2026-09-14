package com.ubuntu.ubuntu_app.application.category.service;

import com.ubuntu.ubuntu_app.application.category.port.out.CategoryRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infrastructure.category.adapter.mapper.CategoryMapper;
import com.ubuntu.ubuntu_app.application.category.api.CategoryRequest;
import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;
import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepositoryPort categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);
        categoryService = new CategoryService(categoryRepository, mapper);
    }

    @Test
    void getAllCategories() {
        CategoryEntity mockEntity = new CategoryEntity(1L, "AGRICULTURA");
        List<CategoryEntity> mockEntityList = List.of(mockEntity);

        Mockito.when(categoryRepository.findAll()).thenReturn(mockEntityList);

        List<CategoryResponse> response = categoryService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("AGRICULTURA", response.get(0).name());
        assertDoesNotThrow(() -> categoryService.findAll());
        Mockito.verify(categoryRepository, times(2)).findAll();
    }

    @Test
    void getAllCategoriesEmpty() {

        Mockito.when(categoryRepository.findAll()).thenReturn(Collections.emptyList());


        assertThrows(SqlEmptyResponse.class, () -> categoryService.findAll());

        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    void createCategoryMapsWithoutId() {
        categoryService.create(new CategoryRequest("AGRO"));

        var captor = ArgumentCaptor.forClass(CategoryEntity.class);
        Mockito.verify(categoryRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("AGRO", captor.getValue().getName());
    }
}
