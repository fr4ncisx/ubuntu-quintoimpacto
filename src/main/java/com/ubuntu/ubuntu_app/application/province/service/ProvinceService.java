package com.ubuntu.ubuntu_app.application.province.service;

import com.ubuntu.ubuntu_app.application.province.port.in.ProvinceUseCase;
import com.ubuntu.ubuntu_app.application.province.port.out.ProvinceRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infrastructure.province.adapter.mapper.ProvinceMapper;
import com.ubuntu.ubuntu_app.application.province.api.ProvinceResponse;
import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;
import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProvinceService implements ProvinceUseCase {

    private final ProvinceRepositoryPort provinceRepository;
    private final ProvinceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceResponse> findProvincesByCountry(String countryName) {
        Optional<CountryEntity> country = provinceRepository.findCountryByName(countryName);
        if (country.isEmpty()) {
            throw new SqlEmptyResponse("Pais no encontrado");
        }
        List<ProvinceEntity> provinces = provinceRepository.findProvincesByCountryId(country.get().getId());
        if (provinces.isEmpty()) {
            throw new SqlEmptyResponse("provincias no encontradas");
        }
        return provinces.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
