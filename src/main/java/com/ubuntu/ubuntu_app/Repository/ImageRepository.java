package com.ubuntu.ubuntu_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    
    @Modifying
    @Query(value = "DELETE FROM Imagenes WHERE id_publicacion IS NULL AND id_micro IS NULL", nativeQuery = true)
    void cleanOrphanImages();
}
