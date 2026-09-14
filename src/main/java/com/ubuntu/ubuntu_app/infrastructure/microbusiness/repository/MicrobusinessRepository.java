package com.ubuntu.ubuntu_app.infrastructure.microbusiness.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;

import java.util.List;

@Repository
public interface MicrobusinessRepository extends JpaRepository<MicrobusinessEntity, Long> {

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.name LIKE %:name% AND active=true")
    List<MicrobusinessEntity> findByNameLike(String name);

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.name LIKE %:name% AND active=true")
    Page<MicrobusinessEntity> findByNameLike(String name, Pageable pageable);

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.active = true AND m.category.id = (SELECT c.id FROM CategoryEntity c WHERE c.name = :category)")
    List<MicrobusinessEntity> findAllActive(String category);

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.active = true AND m.category.id = (SELECT c.id FROM CategoryEntity c WHERE c.name = :category)")
    Page<MicrobusinessEntity> findAllActive(String category, Pageable pageable);

    List<MicrobusinessEntity> findByActiveTrueOrderByCreatedDateDesc();

    List<MicrobusinessEntity> findByActiveFalseOrderByCreatedDateDesc();

    Page<MicrobusinessEntity> findAllByActiveTrue(Pageable pageable);

    Page<MicrobusinessEntity> findAllByActiveFalse(Pageable pageable);

    @Query(value = "SELECT COUNT(id) FROM microbusinesses WHERE active = true AND EXTRACT(YEAR FROM created_at) = :year AND EXTRACT(MONTH FROM created_at) = :month", nativeQuery = true)
    Long findByStatistics(int month, int year);

    @Query(value = "SELECT COUNT(id) FROM microbusinesses WHERE active = true AND category_id = :categoryId AND EXTRACT(YEAR FROM created_at) = :year AND EXTRACT(MONTH FROM created_at) = :month", nativeQuery = true)
    Long findByCategoryStatistics(int month, int year, int categoryId);

    List<MicrobusinessEntity> findByMailedFalse();
}
