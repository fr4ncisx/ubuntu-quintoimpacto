package com.ubuntu.ubuntu_app.Repository;

import com.ubuntu.ubuntu_app.model.ProvinciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity,Long> {

    @Query(value = "SELECT * FROM Provincia p WHERE p.id_pais = :id",nativeQuery = true)
    List<ProvinciaEntity> findProvinceByIDCountry(Long id);
}
