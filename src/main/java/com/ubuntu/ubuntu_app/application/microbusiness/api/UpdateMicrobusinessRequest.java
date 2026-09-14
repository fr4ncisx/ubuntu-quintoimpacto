package com.ubuntu.ubuntu_app.application.microbusiness.api;

import java.util.List;

import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;
import com.ubuntu.ubuntu_app.application.media.api.ImageReference;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateMicrobusinessRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 300) String description,
        @NotBlank @Size(max = 300) String moreInfo,
        @NotBlank String country,
        @NotBlank String province,
        @NotBlank @Size(max = 100, message = "El nombre de la ciudad es demasiado largo") String city,
        @NotNull CategoryResponse category,
        @Size(max = 100, message = "El nombre de la subcategoria es demasiado largo") String subcategory,
        @NotNull List<ImageReference> images) {
}
