package com.ubuntu.ubuntu_app.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.ContactRequestEntity;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequestEntity, Long> {
    List<ContactRequestEntity> findByReviewedTrue();

    List<ContactRequestEntity> findByReviewedFalse();

    @Query(value = "SELECT COUNT(id) FROM contacto WHERE gestionado=:b AND MONTH(fecha_creacion)= :month AND YEAR(fecha_creacion)= :year", nativeQuery = true)
    Long findByStatisticsContact(boolean b, int month, int year);
}
