package com.ubuntu.ubuntu_app.infrastructure.media.adapter.out;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.media.port.out.ImageRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.media.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageJpaAdapter implements ImageRepositoryPort {

    private final ImageRepository repository;

    @Override
    public void cleanOrphanImages() {
        repository.cleanOrphanImages();
    }
}
