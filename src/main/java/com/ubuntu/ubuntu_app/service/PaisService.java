package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.PaisRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.SQLemptyDataException;
import com.ubuntu.ubuntu_app.model.dto.PaisDto;
import com.ubuntu.ubuntu_app.model.entities.PaisEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaisService {

    @Autowired
    private PaisRepository paisRepo;

    public ResponseEntity<?> getAll() {
        List<PaisEntity> paises = paisRepo.findAll();
        if (!paises.isEmpty()) {
            List<PaisDto> paisesDto = paises.stream().map(p -> MapperConverter.generate().map(p, PaisDto.class)).toList();
            return new ResponseEntity<>(paisesDto, HttpStatus.OK);
        } else {
            throw new SQLemptyDataException("No hay Paises");
        }
    }

}
