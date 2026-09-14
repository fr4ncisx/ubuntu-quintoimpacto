package com.ubuntu.ubuntu_app.infrastructure.publication.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.publication.port.out.PublicationRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.repository.PublicationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicationJpaAdapter implements PublicationRepositoryPort {

    private final PublicationRepository repository;

    @Override
    public PublicationEntity save(PublicationEntity publication) {
        return repository.save(publication);
    }

    @Override
    public Optional<PublicationEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(PublicationEntity publication) {
        repository.delete(publication);
    }

    @Override
    public List<PublicationEntity> findByTitleLikeAndActiveTrue(String publication) {
        return repository.findByTitleLikeAndActiveTrue(publication);
    }

    @Override
    public Page<PublicationEntity> findByTitleLikeAndActiveTrue(String publication, Pageable pageable) {
        return repository.findByTitleLikeAndActiveTrue(publication, pageable);
    }

    @Override
    public List<PublicationEntity> findAllByActiveTrueOrderByDateDesc() {
        return repository.findAllByActiveTrueOrderByDateDesc();
    }

    @Override
    public List<PublicationEntity> findAllByActiveFalseOrderByDateDesc() {
        return repository.findAllByActiveFalseOrderByDateDesc();
    }

    @Override
    public Page<PublicationEntity> findAllByActiveTrue(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Override
    public Page<PublicationEntity> findAllByActiveFalse(Pageable pageable) {
        return repository.findAllByActiveFalse(pageable);
    }

    @Override
    public List<PublicationEntity> findByIdCurrentMonthAndActive(int month, int year) {
        return repository.findByIdCurrentMonthAndActive(month, year);
    }
}
