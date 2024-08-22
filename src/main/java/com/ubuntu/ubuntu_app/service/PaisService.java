package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.PaisRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.model.dto.PaisDto;
import com.ubuntu.ubuntu_app.model.entities.PaisEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@RequiredArgsConstructor
@Service
public class PaisService {

    private final PaisRepository paisRepo;

    public ResponseEntity<?> getAll() {
        List<PaisEntity> paises = paisRepo.findAll();
        if (!paises.isEmpty()) {
            List<PaisDto> paisesDto = paises.stream().map(p -> MapperConverter.generate().map(p, PaisDto.class)).toList();
            return new ResponseEntity<>(paisesDto, HttpStatus.OK);
        } else {
            throw new SqlEmptyResponse("No hay Paises");
        }
    }

}
