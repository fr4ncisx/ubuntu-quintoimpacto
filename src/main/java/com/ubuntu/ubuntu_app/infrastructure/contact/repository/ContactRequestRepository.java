package com.ubuntu.ubuntu_app.infrastructure.contact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequestEntity, Long> {
    List<ContactRequestEntity> findByReviewedTrue();

    List<ContactRequestEntity> findByReviewedFalse();

    Page<ContactRequestEntity> findByReviewedTrue(Pageable pageable);

    Page<ContactRequestEntity> findByReviewedFalse(Pageable pageable);

    @Query(value = "SELECT COUNT(id) FROM contact_requests WHERE reviewed = :b AND EXTRACT(MONTH FROM created_at) = :month AND EXTRACT(YEAR FROM created_at) = :year", nativeQuery = true)
    Long findByStatisticsContact(boolean b, int month, int year);
}
