package com.ubuntu.ubuntu_app.application.country.service;

import com.ubuntu.ubuntu_app.application.country.port.in.CountryUseCase;
import com.ubuntu.ubuntu_app.application.country.port.out.CountryRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infrastructure.country.adapter.mapper.CountryMapper;
import com.ubuntu.ubuntu_app.application.country.api.CountryResponse;
import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CountryService implements CountryUseCase {

    private final CountryRepositoryPort countryRepository;
    private final CountryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CountryResponse> findAll() {
        List<CountryEntity> countries = countryRepository.findAll();
        if (countries.isEmpty()) {
            throw new SqlEmptyResponse("No hay Paises");
        }
        return countries.stream().map(mapper::toDto).toList();
    }

}
