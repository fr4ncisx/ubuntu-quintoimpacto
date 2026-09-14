package com.ubuntu.ubuntu_app.infrastructure.category.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.category.port.in.CategoryUseCase;
import com.ubuntu.ubuntu_app.application.category.api.CategoryRequest;
import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryV1Controller {

    private final CategoryUseCase categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> createCategory(
            @RequestBody @Valid CategoryRequest request) {
        return new ResponseEntity<>(
                ApiResponse.created(categoryService.create(request)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> findAllCategories() {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.findAll()));
    }
}
