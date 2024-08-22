package com.ubuntu.ubuntu_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;

import java.util.List;

@Repository
public interface MicrobusinessRepository extends JpaRepository<MicrobusinessEntity, Long> {

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.nombre LIKE %:nombre% AND activo=true")
    List<MicrobusinessEntity> findByIdNombre(String nombre);

    @Query(value = "SELECT m FROM MicrobusinessEntity m WHERE m.activo = true AND m.categoria.id = (SELECT c.id FROM CategoryEntity c WHERE c.nombre = :category)")
    List<MicrobusinessEntity> findAllActive(String category);

    List<MicrobusinessEntity> findByActivoTrue();

    @Query(value = "SELECT COUNT(id) FROM microemprendimientos WHERE EXTRACT(YEAR FROM fecha_creacion) = :year AND EXTRACT(MONTH FROM fecha_creacion) = :month", nativeQuery = true)
    Long findByStatistics(int month, int year);

    @Query(value = "SELECT COUNT(id) FROM microemprendimientos WHERE id_categoria=:categoryId AND EXTRACT(YEAR FROM fecha_creacion) = :year AND EXTRACT(MONTH FROM fecha_creacion) = :month", nativeQuery = true)
    Long findByCategoryStatistics(int month, int year, int categoryId);

    List<MicrobusinessEntity> findByEnviadoPorMailFalse();
}
