package com.ubuntu.ubuntu_app.infrastructure.publication.adapter.out;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.publication.port.out.PublicationViewRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationViewEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.repository.PublicationViewRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicationViewJpaAdapter implements PublicationViewRepositoryPort {

    private final PublicationViewRepository repository;

    @Override
    public PublicationViewEntity save(PublicationViewEntity view) {
        return repository.save(view);
    }

    @Override
    public Long getClickCountActualMonth(Long id, int month, int year) {
        return repository.getClickCountActualMonth(id, month, year);
    }
}
