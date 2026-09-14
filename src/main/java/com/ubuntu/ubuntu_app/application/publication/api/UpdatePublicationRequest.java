package com.ubuntu.ubuntu_app.application.publication.api;

import java.util.List;

import com.ubuntu.ubuntu_app.application.media.api.ImageReference;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePublicationRequest(
        @NotBlank @NotNull @Size(max = 200) String title,
        @NotBlank @NotNull @Size(max = 2000) String description,
        @NotNull List<ImageReference> images) {
}
