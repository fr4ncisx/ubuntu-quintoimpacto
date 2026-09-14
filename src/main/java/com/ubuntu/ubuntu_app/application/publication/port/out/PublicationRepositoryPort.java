package com.ubuntu.ubuntu_app.application.publication.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationEntity;

public interface PublicationRepositoryPort {

    PublicationEntity save(PublicationEntity publication);

    Optional<PublicationEntity> findById(Long id);

    void delete(PublicationEntity publication);

    List<PublicationEntity> findByTitleLikeAndActiveTrue(String publication);

    Page<PublicationEntity> findByTitleLikeAndActiveTrue(String publication, Pageable pageable);

    List<PublicationEntity> findAllByActiveTrueOrderByDateDesc();

    List<PublicationEntity> findAllByActiveFalseOrderByDateDesc();

    Page<PublicationEntity> findAllByActiveTrue(Pageable pageable);

    Page<PublicationEntity> findAllByActiveFalse(Pageable pageable);

    List<PublicationEntity> findByIdCurrentMonthAndActive(int month, int year);
}
