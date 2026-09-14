package com.ubuntu.ubuntu_app.application.microbusiness.api;

import java.util.List;

import com.ubuntu.ubuntu_app.application.category.api.CategoryResponse;
import com.ubuntu.ubuntu_app.application.media.api.ImageReference;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

public record NearbyMicrobusinessResponse(
        Long id,
        String name,
        String description,
        double distance,
        String moreInfo,
        String country,
        String province,
        String city,
        CategoryResponse category,
        String subcategory,
        List<ImageReference> images) {

    public NearbyMicrobusinessResponse(MicrobusinessEntity micro, double distance) {
        this(micro.getId(),
                micro.getName(),
                micro.getDescription(),
                distance,
                micro.getMoreInfo(),
                micro.getCountry(),
                micro.getProvince(),
                micro.getCity(),
                micro.getCategory() == null ? null
                        : new CategoryResponse(micro.getCategory().getName()),
                micro.getSubcategory(),
                micro.getImages() == null ? List.of() : micro.getImages().stream()
                        .map(image -> new ImageReference(image.getUrl()))
                        .toList());
    }
}
