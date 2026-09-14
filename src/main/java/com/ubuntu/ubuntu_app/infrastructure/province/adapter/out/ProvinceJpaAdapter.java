package com.ubuntu.ubuntu_app.infrastructure.province.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.province.port.out.ProvinceRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;
import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;
import com.ubuntu.ubuntu_app.infrastructure.country.repository.CountryRepository;
import com.ubuntu.ubuntu_app.infrastructure.province.repository.ProvinceRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProvinceJpaAdapter implements ProvinceRepositoryPort {

    private final ProvinceRepository provinceRepository;
    private final CountryRepository countryRepository;

    @Override
    public Optional<CountryEntity> findCountryByName(String name) {
        return countryRepository.findByName(name);
    }

    @Override
    public List<ProvinceEntity> findProvincesByCountryId(Long countryId) {
        return provinceRepository.findProvincesByCountryId(countryId);
    }
}
