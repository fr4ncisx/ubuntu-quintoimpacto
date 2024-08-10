package com.ubuntu.ubuntu_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;

import java.util.List;

@Repository
public interface MicrobusinessRepository extends JpaRepository<MicrobusinessEntity, Long>{

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.nombre LIKE :nombre%")
    List<MicrobusinessEntity> findByIdNombre(String nombre);
    
    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.activo = true AND m.categoria.id = (SELECT c.id FROM CategoryEntity c WHERE c.nombre = :category)")
    List<MicrobusinessEntity> findAllActive(String category);

    @Query(value ="DELETE FROM imagenes WHERE id_micro IS NULL" ,nativeQuery = true)
    void cleanOrphanImages();

    List<MicrobusinessEntity> findByActivoTrueOrderByNombreAsc();

}
