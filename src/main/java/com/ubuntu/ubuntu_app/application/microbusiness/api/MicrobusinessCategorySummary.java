package com.ubuntu.ubuntu_app.application.microbusiness.api;

import java.util.List;

import com.ubuntu.ubuntu_app.application.media.api.ImageReference;

public record MicrobusinessCategorySummary(
        Long id,
        String name,
        String description,
        String moreInfo,
        String country,
        String province,
        String city,
        String subcategory,
        List<ImageReference> images) {
}
