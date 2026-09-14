package com.ubuntu.ubuntu_app.application.contact.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.application.contact.port.in.ContactRequestUseCase;
import com.ubuntu.ubuntu_app.shared.clock.ClockPort;
import com.ubuntu.ubuntu_app.application.contact.port.out.ContactRequestRepositoryPort;
import com.ubuntu.ubuntu_app.application.microbusiness.port.out.MicrobusinessRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.IllegalRewriteException;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infrastructure.contact.adapter.mapper.ContactRequestMapper;
import com.ubuntu.ubuntu_app.application.contact.api.CreateContactRequest;
import com.ubuntu.ubuntu_app.application.contact.api.ContactRequestResponse;
import com.ubuntu.ubuntu_app.application.contact.api.ContactRequestSummary;
import com.ubuntu.ubuntu_app.application.contact.api.MicrobusinessReference;
import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContactRequestService implements ContactRequestUseCase {

    private final ContactRequestRepositoryPort contactRequestRepository;
    private final MicrobusinessRepositoryPort microbusinessRepository;
    private final ContactRequestMapper mapper;
    private final ClockPort clock;

    @Override
    @Transactional
    public void create(@Valid CreateContactRequest request, Long id) {
        var microEntity = microbusinessRepository.findById(id);
        if (!microEntity.isPresent()) {
            throw new SqlEmptyResponse("Falló al buscar emprendimiento con ese id");
        }
        contactRequestRepository.save(mapper.toEntity(request, microEntity.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRequestSummary> findReviewed() {
        var listOfReviewed = contactRequestRepository.findByReviewedTrue();
        if (listOfReviewed.isEmpty()) {
            throw new SqlEmptyResponse("No se encontraron solicitudes de contacto gestionadas");
        }
        return toSummary(listOfReviewed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRequestSummary> findUnreviewed() {
        var listOfUnreviewed = contactRequestRepository.findByReviewedFalse();
        if (listOfUnreviewed.isEmpty()) {
            throw new SqlEmptyResponse("No se encontraron solicitudes de contacto sin gestionar");
        }
        return toSummary(listOfUnreviewed);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactRequestSummary> findReviewed(Pageable pageable) {
        return contactRequestRepository.findByReviewedTrue(pageable).map(this::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactRequestSummary> findUnreviewed(Pageable pageable) {
        return contactRequestRepository.findByReviewedFalse(pageable).map(this::toSummary);
    }

    @Override
    @Transactional
    public void markReviewed(Long id) {
        var contactEntity = contactRequestRepository.findById(id);
        if (!contactEntity.isPresent()) {
            throw new SqlEmptyResponse("La solicitud de contacto no existe");
        }
        var obtainedContactRequest = contactEntity.get();
        if (obtainedContactRequest.isReviewed()) {
            throw new IllegalRewriteException("La solicitud de contacto no se puede volver a cambiar a gestionado");
        }
        obtainedContactRequest.setReviewed(true);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactRequestResponse findById(Long id) {
        var contactEntity = contactRequestRepository.findById(id);
        if (!contactEntity.isPresent()) {
            throw new SqlEmptyResponse("La solicitud de contacto no existe");
        }
        return new ContactRequestResponse(contactEntity.get());
    }

    private List<ContactRequestSummary> toSummary(List<ContactRequestEntity> listStatus) {
        return listStatus.stream().map(this::toSummary).toList();
    }

    private ContactRequestSummary toSummary(ContactRequestEntity contactRequest) {
        return new ContactRequestSummary(
                new MicrobusinessReference(contactRequest.getMicrobusiness()), contactRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> countByStatistics() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("Reviewed",
                contactRequestRepository.countByReviewedAndMonth(true, clock.currentMonth(), clock.currentYear()));
        counts.put("Unreviewed",
                contactRequestRepository.countByReviewedAndMonth(false, clock.currentMonth(), clock.currentYear()));
        return counts;
    }

}
