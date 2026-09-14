package com.ubuntu.ubuntu_app.application.category.port.in;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.category.api.CategoryRequest;
import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;

public interface CategoryUseCase {

    Map<String, String> create(CategoryRequest categoryDTO);

    List<CategoryResponse> findAll();
}
