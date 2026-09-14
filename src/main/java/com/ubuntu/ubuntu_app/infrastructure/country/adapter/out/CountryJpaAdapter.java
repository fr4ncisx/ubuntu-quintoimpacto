package com.ubuntu.ubuntu_app.infrastructure.country.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.country.port.out.CountryRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;
import com.ubuntu.ubuntu_app.infrastructure.country.repository.CountryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CountryJpaAdapter implements CountryRepositoryPort {

    private final CountryRepository repository;

    @Override
    public List<CountryEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CountryEntity> findByName(String name) {
        return repository.findByName(name);
    }
}
