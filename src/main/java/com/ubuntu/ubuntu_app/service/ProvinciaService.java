package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.PaisRepository;
import com.ubuntu.ubuntu_app.Repository.ProvinciaRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.SQLemptyDataException;
import com.ubuntu.ubuntu_app.model.PaisEntity;
import com.ubuntu.ubuntu_app.model.ProvinciaEntity;
import com.ubuntu.ubuntu_app.model.dto.ProvinciaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinciaService {
    @Autowired
    private ProvinciaRepository provRepo;
    @Autowired
    private PaisRepository paisRepo;

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
                throw new SQLemptyDataException("provincias no encontradas");
            }
        }
        throw new SQLemptyDataException("Pais no encontrado");
    }

}
