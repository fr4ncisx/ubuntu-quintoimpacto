package com.ubuntu.ubuntu_app.infrastructure.province.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceEntity,Long> {

    @Query(value = "SELECT * FROM provinces p WHERE p.country_id = :id",nativeQuery = true)
    List<ProvinceEntity> findProvincesByCountryId(Long id);
}
