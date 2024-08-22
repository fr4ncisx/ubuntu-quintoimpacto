package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.PaisRepository;
import com.ubuntu.ubuntu_app.Repository.ProvinciaRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.model.dto.ProvinciaDto;
import com.ubuntu.ubuntu_app.model.entities.PaisEntity;
import com.ubuntu.ubuntu_app.model.entities.ProvinciaEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Lazy
@RequiredArgsConstructor
@Service
public class ProvinciaService {

    private final ProvinciaRepository provRepo;
    private final PaisRepository paisRepo;

    public ResponseEntity<?> findProvinceByCountry(String nombrePais) {
        Optional<PaisEntity> pais = paisRepo.findByNombre(nombrePais);
        if (pais.isPresent()) {
            PaisEntity paisEntity = pais.get();
            List<ProvinciaEntity> provincias = provRepo.findProvinceByIDCountry(paisEntity.getId());
            if (!provincias.isEmpty()) {
                List<ProvinciaDto> provinciasDto = provincias.stream()
                        .map(prov -> MapperConverter.generate().map(prov, ProvinciaDto.class))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(provinciasDto, HttpStatus.OK);
            } else {
                throw new SqlEmptyResponse("provincias no encontradas");
            }
        }
        throw new SqlEmptyResponse("Pais no encontrado");
    }

}
