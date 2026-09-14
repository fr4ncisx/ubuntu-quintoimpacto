package com.ubuntu.ubuntu_app.application.country.port.out;

import java.util.List;
import java.util.Optional;

import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;

public interface CountryRepositoryPort {

    List<CountryEntity> findAll();

    Optional<CountryEntity> findByName(String name);
}
