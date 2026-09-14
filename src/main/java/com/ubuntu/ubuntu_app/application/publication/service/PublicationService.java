package com.ubuntu.ubuntu_app.application.publication.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.application.publication.port.in.PublicationUseCase;
import com.ubuntu.ubuntu_app.shared.clock.ClockPort;
import com.ubuntu.ubuntu_app.application.media.port.out.ImageRepositoryPort;
import com.ubuntu.ubuntu_app.application.publication.port.out.PublicationRepositoryPort;
import com.ubuntu.ubuntu_app.application.publication.port.out.PublicationViewRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.IllegalParameterException;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infrastructure.media.adapter.mapper.ImageMapper;
import com.ubuntu.ubuntu_app.infrastructure.publication.adapter.mapper.PublicationMapper;
import com.ubuntu.ubuntu_app.application.publication.api.PublicationResponse;
import com.ubuntu.ubuntu_app.application.media.api.ImageReference;
import com.ubuntu.ubuntu_app.application.publication.api.CreatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.UpdatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.PublicationStatistics;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationViewEntity;
import com.ubuntu.ubuntu_app.shared.support.StringFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PublicationService implements PublicationUseCase {

    private final PublicationRepositoryPort publicationRepository;
    private final PublicationViewRepositoryPort publicationViewRepository;
    private final ImageRepositoryPort imageRepository;
    private final PublicationMapper publicationMapper;
    private final ImageMapper imageMapper;
    private final ClockPort clock;

    @Override
    @Transactional
    public void create(CreatePublicationRequest request) {
        var imageEntities = toImageEntities(request.images());
        publicationRepository.save(publicationMapper.toEntity(request, imageEntities));
    }

    @Override
    @Transactional
    public void update(UpdatePublicationRequest request, Long id) {
        var publicationFound = publicationFinder(id);
        var imageRequests = request.images();
        var currentImages = publicationFound.getImages();
        if (imageRequests.size() == currentImages.size()) {
            for (int i = 0; i < imageRequests.size(); i++) {
                currentImages.get(i).setUrl(imageRequests.get(i).url());
            }
            publicationMapper.updateEntity(request, currentImages, publicationFound);
        } else {
            var convertedImgEntity = toImageEntities(request.images());
            publicationMapper.updateEntity(request, convertedImgEntity, publicationFound);
            imageRepository.cleanOrphanImages();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationResponse findById(Long id) {
        return publicationMapper.toDto(publicationFinder(id));
    }

    @Override
    @Transactional
    public void setVisibility(Long id, boolean enable) {
        var publicationFound = publicationFinder(id);
        publicationFound.setActive(enable);
    }

    @Override
    @Transactional
    public void registerView(Long id) {
        var publicationFound = publicationFinder(id);
        publicationViewRepository.save(new PublicationViewEntity(publicationFound));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationStatistics> getStatistics(Long limitSize) {
        List<PublicationStatistics> statistics = new ArrayList<>();
        if (limitSize <= 0) {
            throw new IllegalParameterException("Expected limitSize above 0");
        }
        var listOfPublicationsFound = publicationRepository.findByIdCurrentMonthAndActive(
                clock.currentMonth(), clock.currentYear());
        if (listOfPublicationsFound.isEmpty()) {
            throw new SqlEmptyResponse("No publications found");
        }
        for (PublicationEntity publicationEntity : listOfPublicationsFound) {
            var count = publicationViewRepository.getClickCountActualMonth(publicationEntity.getId(),
                    clock.currentMonth(), clock.currentYear());
            statistics.add(
                    new PublicationStatistics(publicationEntity.getTitle(), publicationEntity.getDate(), count));
        }
        var sortedStatistics = statistics.stream()
                .sorted(Comparator.comparing(PublicationStatistics::visualizations).reversed()).limit(limitSize)
                .toList();
        if (sortedStatistics.isEmpty()) {
            throw new SqlEmptyResponse("No publications at current month/year");
        }
        return sortedStatistics;
    }

    private PublicationEntity publicationFinder(Long id) {
        var optionalPublication = publicationRepository.findById(id);
        if (optionalPublication.isEmpty()) {
            throw new SqlEmptyResponse("Publication not found");
        } else {
            return optionalPublication.get();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> search(String publication) {
        if (publication.isBlank()) {
            throw new IllegalParameterException("Input is required");
        }
        var searchNormalized = publicationRepository
                .findByTitleLikeAndActiveTrue(StringFilter.getNormalizedInput(publication));
        if (searchNormalized.isEmpty()) {
            var searchWithLowerCase = publicationRepository.findByTitleLikeAndActiveTrue(publication.toLowerCase());
            if (searchWithLowerCase.isEmpty()) {
                throw new SqlEmptyResponse("No publications match with that word");
            }
            return searchWithLowerCase.stream()
                    .map(publicationMapper::toDto)
                    .toList();
        }
        return searchNormalized.stream()
                .map(publicationMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PublicationResponse> search(String publication, Pageable pageable) {
        if (publication.isBlank()) {
            throw new IllegalParameterException("Input is required");
        }
        var normalized = StringFilter.getNormalizedInput(publication);
        var page = publicationRepository.findByTitleLikeAndActiveTrue(normalized, pageable);
        if (page.isEmpty()) {
            var fallback = publicationRepository.findByTitleLikeAndActiveTrue(publication.toLowerCase(),
                    pageable);
            if (fallback.isEmpty()) {
                throw new SqlEmptyResponse("No publications match with that word");
            }
            return fallback.map(publicationMapper::toDto);
        }
        return page.map(publicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> findAll(boolean active) {
        if (active) {
            return searchs(publicationRepository.findAllByActiveTrueOrderByDateDesc());
        } else {
            return searchs(publicationRepository.findAllByActiveFalseOrderByDateDesc());
        }
    }

    @Transactional(readOnly = true)
    public Page<PublicationResponse> findAll(boolean active, Pageable pageable) {
        var page = active
                ? publicationRepository.findAllByActiveTrue(pageable)
                : publicationRepository.findAllByActiveFalse(pageable);
        if (page.isEmpty()) {
            throw new SqlEmptyResponse("No publications found");
        }
        return page.map(publicationMapper::toDto);
    }

    private List<PublicationResponse> searchs(List<PublicationEntity> publications) {
        if (publications.isEmpty()) {
            throw new SqlEmptyResponse("No publications found");
        }
        return publications.stream()
                .map(publicationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == 0) {
            throw new IllegalParameterException("Not acceptable value id zero");
        }
        var optionalPublication = publicationRepository.findById(id);
        if (optionalPublication.isEmpty()) {
            throw new SqlEmptyResponse("Publication not found");
        }
        publicationRepository.delete(optionalPublication.get());
    }

    private List<ImageEntity> toImageEntities(List<ImageReference> images) {
        return images.stream()
                .map(imageMapper::toEntity).toList();
    }
}
