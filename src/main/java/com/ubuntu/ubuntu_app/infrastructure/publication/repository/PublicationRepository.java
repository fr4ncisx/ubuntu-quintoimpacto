package com.ubuntu.ubuntu_app.infrastructure.publication.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationEntity;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Long>{
    
    @Query(value = "SELECT * FROM publications WHERE active = true AND EXTRACT(MONTH FROM created_at) = :month AND EXTRACT(YEAR FROM created_at) = :year", nativeQuery = true)
    List<PublicationEntity> findByIdCurrentMonthAndActive(int month, int year);

    List<PublicationEntity> findAllByActiveTrueOrderByDateDesc();

    List<PublicationEntity> findAllByActiveFalseOrderByDateDesc();

    Page<PublicationEntity> findAllByActiveTrue(Pageable pageable);

    Page<PublicationEntity> findAllByActiveFalse(Pageable pageable);

    List<PublicationEntity> findByTitleLikeAndActiveTrue(String publication);

    Page<PublicationEntity> findByTitleLikeAndActiveTrue(String publication, Pageable pageable);
    
}