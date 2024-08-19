package com.ubuntu.ubuntu_app.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.PublicationEntity;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Long>{
    
    @Query(value = "SELECT * FROM publicaciones WHERE id= :id AND active=true AND EXTRACT(MONTH FROM fecha_creacion) = :month AND EXTRACT(YEAR FROM fecha_creacion) = :year", nativeQuery = true)
    Optional<PublicationEntity> findByIdCurrentMonthAndActive(Long id, int month, int year);

    List<PublicationEntity> findAllByActiveTrueOrderByDateDesc();

    List<PublicationEntity> findByTitleLike(String publication);
    
}