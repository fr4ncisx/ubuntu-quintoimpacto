package com.ubuntu.ubuntu_app.application.publication.api;

import java.time.LocalDate;
import java.util.List;

import com.ubuntu.ubuntu_app.application.media.api.ImageReference;

public record PublicationResponse(
        Long id,
        String title,
        String description,
        LocalDate date,
        List<ImageReference> images) {
}
