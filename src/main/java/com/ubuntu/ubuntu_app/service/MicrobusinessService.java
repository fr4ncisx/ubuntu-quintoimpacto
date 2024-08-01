package com.ubuntu.ubuntu_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubuntu.ubuntu_app.Repository.CategoryRepository;
import com.ubuntu.ubuntu_app.Repository.MicrobusinessRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.EmptyFieldException;
import com.ubuntu.ubuntu_app.infra.errors.SQLemptyDataException;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessCategoryDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessSearchbarDTO;
import com.ubuntu.ubuntu_app.model.entities.CategoryEntity;
import com.ubuntu.ubuntu_app.model.entities.ImageEntity;
import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MicrobusinessService {

    @Autowired
    private MicrobusinessRepository microbusinessRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> create(MicrobusinessDTO microbusinessDTO) {
        Optional<CategoryEntity> categoryOptional = categoryRepository
                .findByNombre(microbusinessDTO.getCategoria().getNombre());
        if (!categoryOptional.isPresent()) {
            var jsonResponse = ResponseMap.createResponse("La categoria no se pudo encontrar");
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
        var imageEntity = microbusinessDTO.getImagenes().stream()
        .map(imgDTO -> MapperConverter.generate().map(imgDTO, ImageEntity.class)).toList();        
        MicrobusinessEntity microEntity = new MicrobusinessEntity(microbusinessDTO, categoryOptional.get(), imageEntity);
        microbusinessRepository.save(microEntity);
        var jsonResponse = ResponseMap.createResponse("Creado exitosamente");
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> update(MicrobusinessDTO microbusinessDTO, Long id) {
        var microSearch = microbusinessRepository.findById(id);
        if (microSearch.isPresent()) {
            var microEntity = microSearch.get();
            var categoryEntity = microEntity.getCategoria();
            var imageDTO = microbusinessDTO.getImagenes();
            var imageEntity = microEntity.getImagenes();
            if (imageDTO.size() == imageEntity.size()) {
                for (int i = 0; i < imageDTO.size(); i++) {
                    imageEntity.get(i).setUrl(imageDTO.get(i).getUrl());
                }
                microEntity.edit(microbusinessDTO, categoryEntity, imageEntity);
            } else {
                var convertedImageEntity = imageDTO.stream()
                        .map(img -> MapperConverter.generate().map(img, ImageEntity.class)).toList();
                microEntity.edit(microbusinessDTO, categoryEntity, convertedImageEntity);
                microbusinessRepository.cleanOrphanImages();
            }
            var jsonResponse = ResponseMap.createResponse("La edición del microemprendimiento fue correcta");
            return new ResponseEntity<>(jsonResponse, HttpStatus.ACCEPTED);
        } else {
            throw new SQLemptyDataException("Microemprendimiento no existe en la base de datos");
        }
    }

    public ResponseEntity<?> findByName(String nombre) {
        if (!nombre.isBlank()) {
            List<MicrobusinessEntity> microBusinessRepo = microbusinessRepository.findByIdNombre(nombre);
            if (!microBusinessRepo.isEmpty()) {
                var response = microBusinessRepo.stream()
                        .map(dto -> MapperConverter.generate().map(dto, MicrobusinessSearchbarDTO.class)).toList();
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                throw new SQLemptyDataException("El nombre empezado por '" + nombre + "' no ha arrojado resultados");
            }
        } else {
            throw new EmptyFieldException("El nombre no debe estar vacio");
        }
    }

    public ResponseEntity<?> findAll(String category) {
        var foundMicro = microbusinessRepository.findAllActive(category);
        if (!foundMicro.isEmpty()) {
            var responseDTO = foundMicro.stream()
                    .map(entity -> MapperConverter.generate().map(entity, MicrobusinessCategoryDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDTO);
        } else {
            throw new SQLemptyDataException("No se encontraron microemprendimientos");
        }
    }

    public ResponseEntity<?> hideMicro(Long id) {
        var microSearch = microbusinessRepository.findById(id);
        if (microSearch.isPresent()) {
            microSearch.get().setActivo(false);
            microbusinessRepository.save(microSearch.get());
            var jsonResponse = ResponseMap.createResponse("El microemprendimiento fue ocultado");
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } else {
            throw new SQLemptyDataException("No se encontro microemprendimiento");
        }
    }

    public ResponseEntity<?> deleteMicro(Long id) {
        var microSearch = microbusinessRepository.findById(id);
        if (microSearch.isPresent()) {
            microbusinessRepository.deleteById(id);
            var jsonResponse = ResponseMap.createResponse("El microemprendimiento fue borrado correctamente");
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } else {
            throw new SQLemptyDataException("No se encontro microemprendimiento");
        }
    }
}
