package com.ubuntu.ubuntu_app.infrastructure.country.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,Long> {

    Optional<CountryEntity> findByName(String countryName);
}
