package com.ubuntu.ubuntu_app.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.PublicationEntity;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Long>{
    
    @Query(value = "SELECT * FROM publicaciones WHERE active=true AND EXTRACT(MONTH FROM fecha_creacion) = :month AND EXTRACT(YEAR FROM fecha_creacion) = :year", nativeQuery = true)
    List<PublicationEntity> findByIdCurrentMonthAndActive(int month, int year);

    List<PublicationEntity> findAllByActiveTrueOrderByDateDesc();

    List<PublicationEntity> findByTitleLikeAndActiveTrue(String publication);
    
}