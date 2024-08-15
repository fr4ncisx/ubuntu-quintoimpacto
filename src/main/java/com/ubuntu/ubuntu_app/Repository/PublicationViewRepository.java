package com.ubuntu.ubuntu_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.PublicationViewEntity;

@Repository
public interface PublicationViewRepository extends JpaRepository<PublicationViewEntity, Long> {

    @Query(value = "SELECT COUNT(id) FROM publicaciones_estadisticas WHERE id_publicacion=:id AND EXTRACT(MONTH FROM fecha_visualizacion) = :month AND EXTRACT(YEAR FROM fecha_visualizacion) = :year", nativeQuery = true)
    Long getClickCountActualMonth(Long id, int month, int year);
}
