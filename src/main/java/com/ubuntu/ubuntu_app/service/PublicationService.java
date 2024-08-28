package com.ubuntu.ubuntu_app.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.Repository.ImageRepository;
import com.ubuntu.ubuntu_app.Repository.PublicationRepository;
import com.ubuntu.ubuntu_app.Repository.PublicationViewRepository;
import com.ubuntu.ubuntu_app.infra.date.GlobalDate;
import com.ubuntu.ubuntu_app.infra.errors.IllegalParameterException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.PublicationDTO;
import com.ubuntu.ubuntu_app.model.dto.PublicationRequestDTO;
import com.ubuntu.ubuntu_app.model.dto.PublicationStatisticsDTO;
import com.ubuntu.ubuntu_app.model.entities.ImageEntity;
import com.ubuntu.ubuntu_app.model.entities.PublicationEntity;
import com.ubuntu.ubuntu_app.model.entities.PublicationViewEntity;
import com.ubuntu.ubuntu_app.model.filters.StringFilter;

import lombok.RequiredArgsConstructor;

@Lazy
@RequiredArgsConstructor
@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationViewRepository publicationViewRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ResponseEntity<?> create(PublicationRequestDTO publicationsDTO) {
        var imageEntity = publicationsDTO.getImagenes().stream()
                .map(imgDTO -> modelMapper.map(imgDTO, ImageEntity.class)).toList();
        PublicationEntity publicationEntity = new PublicationEntity(publicationsDTO, imageEntity);
        publicationRepository.save(publicationEntity);
        var jsonResponse = ResponseMap.createResponse("Publicación creada");
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> update(PublicationRequestDTO publicationsDTO, Long id) {
        var publicationFound = publicationFinder(id);
        var imageDTO = publicationsDTO.getImagenes();
        var imageEntity = publicationFound.getImagenes();
        if (imageDTO.size() == imageEntity.size()) {
            for (int i = 0; i < imageDTO.size(); i++) {
                imageEntity.get(i).setUrl(imageDTO.get(i).getUrl());
            }
            publicationFound.edit(publicationsDTO, imageEntity);
        } else {
            var convertedImgEntity = publicationsDTO.getImagenes().stream()
                    .map(img -> modelMapper.map(img, ImageEntity.class)).toList();
            publicationFound.edit(publicationsDTO, convertedImgEntity);
            imageRepository.cleanOrphanImages();
        }
        return ResponseEntity.ok(ResponseMap.createResponse("Updated succesfully"));
    }

    public ResponseEntity<?> findById(Long id) {
        var publicationFound = publicationFinder(id);
        var jsonResponse = modelMapper.map(publicationFound, PublicationDTO.class);
        return ResponseEntity.ok(jsonResponse);
    }

    @Transactional
    public ResponseEntity<?> hideOrEnablePublication(Long id, boolean enable) {
        var publicationFound = publicationFinder(id);
        if(enable){
            publicationFound.setActive(true);
            return ResponseEntity.ok(ResponseMap.createResponse("Publication reactivated"));
        } else {
            publicationFound.setActive(false);
            return ResponseEntity.ok(ResponseMap.createResponse("Hidden publication sucessfully"));
        }        
    }

    @Transactional
    public ResponseEntity<?> viewed(Long id) {
        var publicationFound = publicationFinder(id);
        PublicationViewEntity view = new PublicationViewEntity(publicationFound);
        publicationViewRepository.save(view);
        return ResponseEntity.ok(ResponseMap.createResponse("Added new visualization"));
    }

    public ResponseEntity<?> getStatistics(Long limitSize) {
        List<PublicationStatisticsDTO> statistics = new ArrayList<>();
        if (limitSize <= 0) {
            throw new IllegalParameterException("Expected limitSize above 0");
        }
        var listOfPublicationsFound = publicationRepository.findByIdCurrentMonthAndActive(GlobalDate.getCurrentMonth(),
                GlobalDate.getCurrentYear());
        if (listOfPublicationsFound.isEmpty()) {
            throw new SqlEmptyResponse("No publications found");
        }
        for (PublicationEntity publicationEntity : listOfPublicationsFound) {
            var count = publicationViewRepository.getClickCountActualMonth(publicationEntity.getId(),
                    GlobalDate.getCurrentMonth(), GlobalDate.getCurrentYear());
            statistics.add(
                    new PublicationStatisticsDTO(publicationEntity.getTitle(), publicationEntity.getDate(), count));
        }
        var sortedStatistics = statistics.stream()
                .sorted(Comparator.comparing(PublicationStatisticsDTO::getVisualizations).reversed()).limit(limitSize)
                .toList();
        if (sortedStatistics.isEmpty()) {
            throw new SqlEmptyResponse("No publications at current month/year");
        }
        return new ResponseEntity<>(sortedStatistics, HttpStatus.OK);
    }

    private PublicationEntity publicationFinder(Long id) {
        var optionalPublication = publicationRepository.findById(id);
        if (!optionalPublication.isPresent()) {
            throw new SqlEmptyResponse("Publication not found");
        } else {
            return optionalPublication.get();
        }
    }

    public ResponseEntity<?> findPublication(String publication) {
        if (publication.isBlank() || publication == null) {
            throw new IllegalParameterException("Input is required");
        }
        var searchNormalized = publicationRepository
                .findByTitleLikeAndActiveTrue(StringFilter.getNormalizedInput(publication));
        if (searchNormalized.isEmpty()) {
            var searchWithLowerCase = publicationRepository.findByTitleLikeAndActiveTrue(publication.toLowerCase());
            if (searchNormalized.isEmpty()) {
                throw new SqlEmptyResponse("No publications match with that word");
            } else {
                var responseLowerCase = searchWithLowerCase.stream()
                        .map(list -> modelMapper.map(list, PublicationDTO.class));
                return ResponseEntity.ok(responseLowerCase);
            }
        }
        var responseDTO = searchNormalized.stream()
                .map(list -> modelMapper.map(list, PublicationDTO.class));
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<?> findAllPublications(boolean active) {
        if (active) {
            return findPublications(publicationRepository.findAllByActiveTrueOrderByDateDesc());
        } else {
            return findPublications(publicationRepository.findAllByActiveFalseOrderByDateDesc());
        }
    }

    private ResponseEntity<?> findPublications(List<PublicationEntity> GenericListOfActiveOrInactive) {
        var listOfPublications = GenericListOfActiveOrInactive;
        if (listOfPublications.isEmpty()) {
            throw new SqlEmptyResponse("No publications found");
        }
        var responseDTO = listOfPublications.stream()
                .map(entity -> modelMapper.map(entity, PublicationDTO.class)).toList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        if (id == 0) {
            throw new IllegalParameterException("Not acceptable value id zero");
        }
        var optionalPublication = publicationRepository.findById(id);
        if (!optionalPublication.isPresent()) {
            throw new SqlEmptyResponse("Publication not found");
        }
        publicationRepository.delete(optionalPublication.get());
        return ResponseEntity.ok(ResponseMap.createResponse("Publication deleted"));
    }
}
