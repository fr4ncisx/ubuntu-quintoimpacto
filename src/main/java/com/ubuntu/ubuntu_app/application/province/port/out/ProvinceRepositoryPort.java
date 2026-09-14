package com.ubuntu.ubuntu_app.application.province.port.out;

import java.util.List;
import java.util.Optional;

import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;
import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;

public interface ProvinceRepositoryPort {

    Optional<CountryEntity> findCountryByName(String name);

    List<ProvinceEntity> findProvincesByCountryId(Long countryId);
}
