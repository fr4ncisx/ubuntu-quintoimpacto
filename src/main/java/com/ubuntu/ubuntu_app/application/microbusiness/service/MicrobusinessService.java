package com.ubuntu.ubuntu_app.application.microbusiness.service;

import com.ubuntu.ubuntu_app.application.microbusiness.port.in.MicrobusinessUseCase;
import com.ubuntu.ubuntu_app.application.category.port.out.CategoryRepositoryPort;
import com.ubuntu.ubuntu_app.shared.clock.ClockPort;
import com.ubuntu.ubuntu_app.application.microbusiness.port.out.GeoPort;
import com.ubuntu.ubuntu_app.application.media.port.out.ImageRepositoryPort;
import com.ubuntu.ubuntu_app.application.microbusiness.port.out.MicrobusinessRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.EmptyFieldException;
import com.ubuntu.ubuntu_app.shared.error.IllegalParameterException;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infrastructure.media.adapter.mapper.ImageMapper;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.adapter.mapper.MicrobusinessMapper;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessCategorySummary;
import com.ubuntu.ubuntu_app.application.microbusiness.api.CreateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.UpdateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.media.api.ImageReference;
import com.ubuntu.ubuntu_app.application.microbusiness.api.NearbyMicrobusinessResponse;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessSummary;
import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;
import com.ubuntu.ubuntu_app.shared.support.StringFilter;
import com.ubuntu.ubuntu_app.shared.geo.GeoLocationService.Nominatim;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MicrobusinessService implements MicrobusinessUseCase {

    private static final long LIMIT_MAX = 10;
    private final MicrobusinessRepositoryPort microbusinessRepository;
    private final CategoryRepositoryPort categoryRepository;
    private final ImageRepositoryPort imageRepository;
    private final GeoPort geoPort;
    private final MicrobusinessMapper microbusinessMapper;
    private final ImageMapper imageMapper;
    private final ClockPort clock;

    @Override
    @Transactional
    public void create(CreateMicrobusinessRequest request) {
        Optional<CategoryEntity> categoryOptional = categoryRepository
                .findByName(request.category().name());
        if (!categoryOptional.isPresent()) {
            throw new IllegalParameterException("La categoria no se pudo encontrar");
        }
        var imageEntities = toImageEntities(request.images());
        microbusinessRepository.save(
                microbusinessMapper.toEntity(request, categoryOptional.get(), imageEntities));
    }

    @Override
    @Transactional
    public void update(UpdateMicrobusinessRequest request, Long id) {
        var microSearch = microbusinessRepository.findById(id);
        if (!microSearch.isPresent()) {
            throw new SqlEmptyResponse("Microemprendimiento no existe en la base de datos");
        }
        var microEntity = microSearch.get();
        var categoryEntity = microEntity.getCategory();
        var imageRequests = request.images();
        var currentImages = microEntity.getImages();
        if (imageRequests.size() == currentImages.size()) {
            for (int i = 0; i < imageRequests.size(); i++) {
                currentImages.get(i).setUrl(imageRequests.get(i).url());
            }
            microbusinessMapper.updateEntity(request, categoryEntity, currentImages, microEntity);
        } else {
            var convertedImageEntity = toImageEntities(request.images());
            microbusinessMapper.updateEntity(request, categoryEntity, convertedImageEntity, microEntity);
            imageRepository.cleanOrphanImages();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MicrobusinessSummary> findByName(String name) {
        if (name.isBlank()) {
            throw new EmptyFieldException("El nombre no debe estar vacio");
        }
        var normalizedInput = StringFilter.getNormalizedInput(name);
        List<MicrobusinessEntity> microBusinessRepo = microbusinessRepository.findByNameLike(normalizedInput);
        if (microBusinessRepo.isEmpty()) {
            throw new SqlEmptyResponse("Microbusiness not found");
        }
        return microBusinessRepo.stream()
                .map(microbusinessMapper::toSearchbarDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<MicrobusinessSummary> findByName(String name, Pageable pageable) {
        if (name.isBlank()) {
            throw new EmptyFieldException("El nombre no debe estar vacio");
        }
        return microbusinessRepository.findByNameLike(StringFilter.getNormalizedInput(name), pageable)
                .map(microbusinessMapper::toSearchbarDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MicrobusinessCategorySummary> findAll(String category) {
        var foundMicro = microbusinessRepository.findAllActive(category);
        if (foundMicro.isEmpty()) {
            throw new SqlEmptyResponse("No se encontraron microemprendimientos");
        }
        return foundMicro.stream()
                .map(microbusinessMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MicrobusinessCategorySummary> findAll(String category, Pageable pageable) {
        return microbusinessRepository.findAllActive(category, pageable)
                .map(microbusinessMapper::toCategoryDto);
    }

    @Override
    @Transactional
    public void setVisibility(Long id, boolean enable) {
        setActive(id, enable);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var microSearch = microbusinessRepository.findById(id);
        if (!microSearch.isPresent()) {
            throw new SqlEmptyResponse("No se encontro microemprendimiento");
        }
        microbusinessRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MicrobusinessSummary> findAllActive() {
        var microSearch = microbusinessRepository.findByActiveTrueOrderByCreatedDateDesc();
        if (microSearch.isEmpty()) {
            throw new SqlEmptyResponse("No se encontaron emprendimientos en la base de datos");
        }
        return microSearch.stream()
                .map(microbusinessMapper::toSearchbarDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<MicrobusinessSummary> findAllActive(Pageable pageable) {
        return microbusinessRepository.findAllByActiveTrue(pageable)
                .map(microbusinessMapper::toSearchbarDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByCurrentMonth() {
        return microbusinessRepository.countByStatistics(clock.currentMonth(), clock.currentYear());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> countByCategoryCurrentMonth() {
        Map<String, Long> countsByCategory = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            countsByCategory.put("cat:" + i, microbusinessRepository
                    .countByCategoryStatistics(clock.currentMonth(), clock.currentYear(), i));
        }
        return countsByCategory;
    }

    @Override
    @Transactional
    public List<CreateMicrobusinessRequest> findUnmailed() {
        List<MicrobusinessEntity> microSearch = microbusinessRepository.findByMailedFalse();
        markAsMailed(microSearch);
        return microSearch.stream().map(microbusinessMapper::toCreateRequest)
                .collect(Collectors.toList());
    }

    private void markAsMailed(List<MicrobusinessEntity> microsEntity) {
        for (MicrobusinessEntity m : microsEntity) {
            m.setMailed(true);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MicrobusinessSummary> findByActive(boolean active) {
        var micros = active
                ? microbusinessRepository.findByActiveTrueOrderByCreatedDateDesc()
                : microbusinessRepository.findByActiveFalseOrderByCreatedDateDesc();
        if (micros.isEmpty()) {
            throw new SqlEmptyResponse("No micro found");
        }
        return micros.stream()
                .map(microbusinessMapper::toSearchbarDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<MicrobusinessSummary> findByActive(boolean active, Pageable pageable) {
        var page = active
                ? microbusinessRepository.findAllByActiveTrue(pageable)
                : microbusinessRepository.findAllByActiveFalse(pageable);
        if (page.isEmpty()) {
            throw new SqlEmptyResponse("No micro found");
        }
        return page.map(microbusinessMapper::toSearchbarDto);
    }

    private void setActive(Long id, boolean active) {
        var microSearch = microbusinessRepository.findById(id);
        if (!microSearch.isPresent()) {
            throw new SqlEmptyResponse("Micro not found");
        }
        microSearch.get().setActive(active);
        microbusinessRepository.save(microSearch.get());
    }

    @Override
    @Cacheable(value = "coordinatesCache", key = "#lat + ',' + #lon")
    @Transactional(readOnly = true)
    public List<NearbyMicrobusinessResponse> findNearby(double lat, double lon) {
        var listOfMicro = microbusinessRepository.findByActiveTrueOrderByCreatedDateDesc();
        if (listOfMicro.isEmpty()) {
            throw new SqlEmptyResponse("No micro found");
        }
        return listOfMicro.stream()
            .filter(micro -> micro.getCity() != null && micro.getCountry() != null)
            .map(micro -> {
                Nominatim coordinates = geoPort.getCoordinatesByName(micro.getProvince(), micro.getCity(), micro.getCountry());
                if(coordinates == null){
                    return null;
                }
                double distance = geoPort.calculate(lat, lon, coordinates.lat(), coordinates.lon());
                return new NearbyMicrobusinessResponse(micro, distance);
            })
                .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(NearbyMicrobusinessResponse::distance))
            .limit(LIMIT_MAX)
                .toList();
    }

    private List<ImageEntity> toImageEntities(List<ImageReference> images) {
        return images.stream()
                .map(imageMapper::toEntity).toList();
    }
}
