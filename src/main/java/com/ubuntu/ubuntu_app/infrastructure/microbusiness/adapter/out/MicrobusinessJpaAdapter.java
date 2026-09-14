package com.ubuntu.ubuntu_app.infrastructure.microbusiness.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.microbusiness.port.out.MicrobusinessRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.repository.MicrobusinessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MicrobusinessJpaAdapter implements MicrobusinessRepositoryPort {

    private final MicrobusinessRepository repository;

    @Override
    public MicrobusinessEntity save(MicrobusinessEntity microbusiness) {
        return repository.save(microbusiness);
    }

    @Override
    public Optional<MicrobusinessEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<MicrobusinessEntity> findByNameLike(String name) {
        return repository.findByNameLike(name);
    }

    @Override
    public Page<MicrobusinessEntity> findByNameLike(String name, Pageable pageable) {
        return repository.findByNameLike(name, pageable);
    }

    @Override
    public List<MicrobusinessEntity> findAllActive(String category) {
        return repository.findAllActive(category);
    }

    @Override
    public Page<MicrobusinessEntity> findAllActive(String category, Pageable pageable) {
        return repository.findAllActive(category, pageable);
    }

    @Override
    public List<MicrobusinessEntity> findByActiveTrueOrderByCreatedDateDesc() {
        return repository.findByActiveTrueOrderByCreatedDateDesc();
    }

    @Override
    public List<MicrobusinessEntity> findByActiveFalseOrderByCreatedDateDesc() {
        return repository.findByActiveFalseOrderByCreatedDateDesc();
    }

    @Override
    public Page<MicrobusinessEntity> findAllByActiveTrue(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Override
    public Page<MicrobusinessEntity> findAllByActiveFalse(Pageable pageable) {
        return repository.findAllByActiveFalse(pageable);
    }

    @Override
    public List<MicrobusinessEntity> findByMailedFalse() {
        return repository.findByMailedFalse();
    }

    @Override
    public Long countByStatistics(int month, int year) {
        return repository.findByStatistics(month, year);
    }

    @Override
    public Long countByCategoryStatistics(int month, int year, int categoryId) {
        return repository.findByCategoryStatistics(month, year, categoryId);
    }
}
