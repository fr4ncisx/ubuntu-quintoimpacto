package com.ubuntu.ubuntu_app.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.ContactRequestEntity;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequestEntity, Long> {
    List<ContactRequestEntity> findByReviewedTrue();

    List<ContactRequestEntity> findByReviewedFalse();
}
