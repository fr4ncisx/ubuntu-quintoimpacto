package com.ubuntu.ubuntu_app.application.microbusiness.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

public interface MicrobusinessRepositoryPort {

    MicrobusinessEntity save(MicrobusinessEntity microbusiness);

    Optional<MicrobusinessEntity> findById(Long id);

    void deleteById(Long id);

    List<MicrobusinessEntity> findByNameLike(String name);

    Page<MicrobusinessEntity> findByNameLike(String name, Pageable pageable);

    List<MicrobusinessEntity> findAllActive(String category);

    Page<MicrobusinessEntity> findAllActive(String category, Pageable pageable);

    List<MicrobusinessEntity> findByActiveTrueOrderByCreatedDateDesc();

    List<MicrobusinessEntity> findByActiveFalseOrderByCreatedDateDesc();

    Page<MicrobusinessEntity> findAllByActiveTrue(Pageable pageable);

    Page<MicrobusinessEntity> findAllByActiveFalse(Pageable pageable);

    List<MicrobusinessEntity> findByMailedFalse();

    Long countByStatistics(int month, int year);

    Long countByCategoryStatistics(int month, int year, int categoryId);
}
