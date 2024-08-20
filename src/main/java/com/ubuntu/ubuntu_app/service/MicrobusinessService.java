package com.ubuntu.ubuntu_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubuntu.ubuntu_app.Repository.CategoryRepository;
import com.ubuntu.ubuntu_app.Repository.ImageRepository;
import com.ubuntu.ubuntu_app.Repository.MicrobusinessRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.date.GlobalDate;
import com.ubuntu.ubuntu_app.infra.errors.EmptyFieldException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessCategoryDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessSearchbarDTO;
import com.ubuntu.ubuntu_app.model.entities.CategoryEntity;
import com.ubuntu.ubuntu_app.model.entities.ImageEntity;
import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MicrobusinessService {

    @Autowired
    private MicrobusinessRepository microbusinessRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageRepository imageRepository;

    public ResponseEntity<?> create(MicrobusinessDTO microbusinessDTO) {
        Optional<CategoryEntity> categoryOptional = categoryRepository
                .findByNombre(microbusinessDTO.getCategoria().getNombre());
        if (!categoryOptional.isPresent()) {
            var jsonResponse = ResponseMap.createResponse("La categoria no se pudo encontrar");
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
        var imageEntity = microbusinessDTO.getImagenes().stream()
                .map(imgDTO -> MapperConverter.generate().map(imgDTO, ImageEntity.class)).toList();
        MicrobusinessEntity microEntity = new MicrobusinessEntity(microbusinessDTO, categoryOptional.get(),
                imageEntity);
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
                imageRepository.cleanOrphanImages();
            }
            var jsonResponse = ResponseMap.createResponse("La edición del microemprendimiento fue correcta");
            return new ResponseEntity<>(jsonResponse, HttpStatus.ACCEPTED);
        } else {
            throw new SqlEmptyResponse("Microemprendimiento no existe en la base de datos");
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
                throw new SqlEmptyResponse("El nombre '" + nombre + "' no ha arrojado resultados");
            }
        } else {
            throw new EmptyFieldException("El nombre no debe estar vacio");
        }
    }

    public ResponseEntity<?> findByCategory(String category) {
        var foundMicro = microbusinessRepository.findAllActive(category);
        if (!foundMicro.isEmpty()) {
            var responseDTO = foundMicro.stream()
                    .map(entity -> MapperConverter.generate().map(entity, MicrobusinessCategoryDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDTO);
        } else {
            throw new SqlEmptyResponse("No se encontraron microemprendimientos");
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
            throw new SqlEmptyResponse("No se encontro microemprendimiento");
        }
    }

    public ResponseEntity<?> deleteMicro(Long id) {
        var microSearch = microbusinessRepository.findById(id);
        if (microSearch.isPresent()) {
            microbusinessRepository.deleteById(id);
            var jsonResponse = ResponseMap.createResponse("El microemprendimiento fue borrado correctamente");
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } else {
            throw new SqlEmptyResponse("No se encontro microemprendimiento");
        }
    }

    public ResponseEntity<?> getAllMicro() {
        var microSearch = microbusinessRepository.findByActivoTrueOrderByNombreAsc();
        if (microSearch.isEmpty()) {
            throw new SqlEmptyResponse("No se encontaron emprendimientos en la base de datos");
        }
        var jsonResponse = microSearch.stream()
        .map(micro -> MapperConverter.generate().map(micro, MicrobusinessSearchbarDTO.class)).toList();
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> findAllMicroCurrentMonth() {
        var microFoundThisMonth = microbusinessRepository.findByStatistics(GlobalDate.getCurrentMonth(), GlobalDate.getCurrentYear());
        return ResponseEntity.ok(ResponseMap.responseGeneric("Found", microFoundThisMonth));
    }

    public ResponseEntity<?> findAllMicroCategoriesCurrentMonth() {
        Map<String, Long> listOfStatisticsByCategory = new LinkedHashMap<>();        
        for (int i = 1; i <= 4; i++) {
            listOfStatisticsByCategory.put("cat:"+ i, microbusinessRepository.findByCategoryStatistics(GlobalDate.getCurrentMonth(), GlobalDate.getCurrentYear(), i));
        }
        return ResponseEntity.ok(ResponseMap.responseGeneric("Found", listOfStatisticsByCategory));
    }
}
