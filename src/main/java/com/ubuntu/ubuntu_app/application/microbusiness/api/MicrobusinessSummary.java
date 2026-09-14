package com.ubuntu.ubuntu_app.application.microbusiness.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;
import com.ubuntu.ubuntu_app.application.media.api.ImageReference;

@JsonPropertyOrder({ "id", "name", "description", "moreInfo", "country", "province", "city",
        "category", "subcategory", "images" })
public record MicrobusinessSummary(
        Long id,
        String name,
        String description,
        String moreInfo,
        String country,
        String province,
        String city,
        CategoryResponse category,
        String subcategory,
        List<ImageReference> images) {
}
