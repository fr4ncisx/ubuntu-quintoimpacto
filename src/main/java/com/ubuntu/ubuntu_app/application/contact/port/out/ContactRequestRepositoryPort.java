package com.ubuntu.ubuntu_app.application.contact.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;

public interface ContactRequestRepositoryPort {

    ContactRequestEntity save(ContactRequestEntity request);

    List<ContactRequestEntity> findByReviewedTrue();

    List<ContactRequestEntity> findByReviewedFalse();

    Page<ContactRequestEntity> findByReviewedTrue(Pageable pageable);

    Page<ContactRequestEntity> findByReviewedFalse(Pageable pageable);

    Optional<ContactRequestEntity> findById(Long id);

    Long countByReviewedAndMonth(boolean reviewed, int month, int year);
}
