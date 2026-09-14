package com.ubuntu.ubuntu_app.infrastructure.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    
    @Modifying
    @Query(value = "DELETE FROM images WHERE publication_id IS NULL AND microbusiness_id IS NULL", nativeQuery = true)
    void cleanOrphanImages();
}
