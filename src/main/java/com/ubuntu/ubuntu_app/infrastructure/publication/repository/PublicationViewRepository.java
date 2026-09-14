package com.ubuntu.ubuntu_app.infrastructure.publication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationViewEntity;

@Repository
public interface PublicationViewRepository extends JpaRepository<PublicationViewEntity, Long>{
    
    @Query(value = "SELECT COUNT(id) FROM publication_views WHERE publication_id = :id AND EXTRACT(MONTH FROM viewed_at) = :month AND EXTRACT(YEAR FROM viewed_at) = :year", nativeQuery = true)
    Long getClickCountActualMonth(Long id, int month, int year);
}
