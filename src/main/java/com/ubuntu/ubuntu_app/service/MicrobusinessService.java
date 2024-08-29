package com.ubuntu.ubuntu_app.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubuntu.ubuntu_app.Repository.CategoryRepository;
import com.ubuntu.ubuntu_app.Repository.ImageRepository;
import com.ubuntu.ubuntu_app.Repository.MicrobusinessRepository;
import com.ubuntu.ubuntu_app.infra.date.GlobalDate;
import com.ubuntu.ubuntu_app.infra.errors.EmptyFieldException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessCategoryDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessGeoDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessSearchbarDTO;
import com.ubuntu.ubuntu_app.model.entities.CategoryEntity;
import com.ubuntu.ubuntu_app.model.entities.ImageEntity;
import com.ubuntu.ubuntu_app.model.entities.MicrobusinessEntity;
import com.ubuntu.ubuntu_app.model.filters.StringFilter;
import com.ubuntu.ubuntu_app.service.geo.GeoDistanceService;
import com.ubuntu.ubuntu_app.service.geo.GeoLocationService.Nominatim;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Lazy
@RequiredArgsConstructor
@Service
public class MicrobusinessService {

    private static final long LIMIT_MAX = 10;
    private final MicrobusinessRepository microbusinessRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final GeoDistanceService geoDistanceService;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> create(MicrobusinessDTO microbusinessDTO) {
        Optional<CategoryEntity> categoryOptional = categoryRepository
                .findByNombre(microbusinessDTO.getCategoria().getNombre());
        if (!categoryOptional.isPresent()) {
            var jsonResponse = ResponseMap.createResponse("La categoria no se pudo encontrar");
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
        var imageEntity = microbusinessDTO.getImagenes().stream()
                .map(imgDTO -> modelMapper.map(imgDTO, ImageEntity.class)).toList();
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
                        .map(img -> modelMapper.map(img, ImageEntity.class)).toList();
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
        if (nombre.isBlank()) {
            throw new EmptyFieldException("El nombre no debe estar vacio");
        }
        var normalizedInput = StringFilter.getNormalizedInput(nombre);
        List<MicrobusinessEntity> microBusinessRepo = microbusinessRepository.findByIdNombre(normalizedInput);
        if (microBusinessRepo.isEmpty()) {
            throw new SqlEmptyResponse("Microbusiness not found");
        }
        var response = microBusinessRepo.stream()
                .map(dto -> modelMapper.map(dto, MicrobusinessSearchbarDTO.class)).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findAll(String category) {
        var foundMicro = microbusinessRepository.findAllActive(category);
        if (!foundMicro.isEmpty()) {
            var responseDTO = foundMicro.stream()
                    .map(entity -> modelMapper.map(entity, MicrobusinessCategoryDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDTO);
        } else {
            throw new SqlEmptyResponse("No se encontraron microemprendimientos");
        }
    }

    public ResponseEntity<?> hideMicro(Long id, boolean enable) {
        if (!enable) {
            return setActive(id, false, "El microemprendimiento fue ocultado");
        } else {
            return setActive(id, true, "El microemprendimiento fue activado");
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
        var microSearch = microbusinessRepository.findByActivoTrueOrderByFechaDesc();
        if (microSearch.isEmpty()) {
            throw new SqlEmptyResponse("No se encontaron emprendimientos en la base de datos");
        }
        var jsonResponse = microSearch.stream()
                .map(micro -> modelMapper.map(micro, MicrobusinessSearchbarDTO.class)).toList();
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> findAllMicroCurrentMonth() {
        var microFoundThisMonth = microbusinessRepository.findByStatistics(GlobalDate.getCurrentMonth(),
                GlobalDate.getCurrentYear());
        return ResponseEntity.ok(ResponseMap.responseGeneric("Found", microFoundThisMonth));
    }

    public ResponseEntity<?> findAllMicroCategoriesCurrentMonth() {
        Map<String, Long> listOfStatisticsByCategory = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            listOfStatisticsByCategory.put("cat:" + i, microbusinessRepository
                    .findByCategoryStatistics(GlobalDate.getCurrentMonth(), GlobalDate.getCurrentYear(), i));
        }
        return ResponseEntity.ok(ResponseMap.responseGeneric("Found", listOfStatisticsByCategory));
    }

    @Transactional
    public List<MicrobusinessDTO> microsNotSent() {
        List<MicrobusinessEntity> microSearch = microbusinessRepository.findByEnviadoPorMailFalse();
        microsTosent(microSearch);
        return microSearch.stream().map(entity -> modelMapper.map(entity, MicrobusinessDTO.class))
                .collect(Collectors.toList());
    }

    private void microsTosent(List<MicrobusinessEntity> microsEntity) {
        for (MicrobusinessEntity m : microsEntity) {
            m.setEnviadoPorMail(true);
        }
    }

    public ResponseEntity<?> findAllMicroByActive(boolean active) {
        if (active) {
            var listOfActiveMicros = microbusinessRepository.findByActivoTrueOrderByFechaDesc();
            if (listOfActiveMicros.isEmpty()) {
                throw new SqlEmptyResponse("No micro found");
            }
            var response = listOfActiveMicros.stream()
                    .map(a -> modelMapper.map(a, MicrobusinessSearchbarDTO.class)).toList();
            return ResponseEntity.ok(response);
        } else {
            var listOfInactiveMicros = microbusinessRepository.findByActivoFalseOrderByFechaDesc();
            if (listOfInactiveMicros.isEmpty()) {
                throw new SqlEmptyResponse("No micro found");
            }
            var response = listOfInactiveMicros.stream()
                    .map(a -> modelMapper.map(a, MicrobusinessSearchbarDTO.class)).toList();
            return ResponseEntity.ok(response);
        }
    }

    private ResponseEntity<?> setActive(Long id, boolean b, String message) {
        var microSearch = microbusinessRepository.findById(id);
        if (!microSearch.isPresent()) {
            throw new SqlEmptyResponse("Micro not found");
        }
        microSearch.get().setActivo(b);
        microbusinessRepository.save(microSearch.get());
        var jsonResponse = ResponseMap.createResponse(message);
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @Cacheable(value = "coordinatesCache", key = "#lat + ',' + #lon")
    public ResponseEntity<?> getNearMicro(double lat, double lon) {
        var listOfMicro = microbusinessRepository.findByActivoTrueOrderByFechaDesc();
        if (listOfMicro.isEmpty()) {
            throw new SqlEmptyResponse("No micro found");
        }
        List<MicrobusinessGeoDTO> listGeo = listOfMicro.stream()
            .filter(micro -> micro.getCiudad() != null && micro.getPais() != null)
            .map(micro -> {
                Nominatim coordinates = geoDistanceService.getCoordinatesByName(micro.getProvincia(), micro.getCiudad(), micro.getPais());
                if(coordinates == null){
                    return null;
                }
                double distance = geoDistanceService.calculate(lat, lon, coordinates.lat(), coordinates.lon());
                return new MicrobusinessGeoDTO(micro, distance);                
            })
            .filter(dto -> dto != null)
            .sorted(Comparator.comparingDouble(MicrobusinessGeoDTO::getDistance))
            .limit(LIMIT_MAX)
            .collect(Collectors.toList());

        return ResponseEntity.ok(listGeo);
    }
}
