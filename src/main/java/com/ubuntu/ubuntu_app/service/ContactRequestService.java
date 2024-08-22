package com.ubuntu.ubuntu_app.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubuntu.ubuntu_app.Repository.ContactRequestRepository;
import com.ubuntu.ubuntu_app.Repository.MicrobusinessRepository;
import com.ubuntu.ubuntu_app.infra.date.GlobalDate;
import com.ubuntu.ubuntu_app.infra.errors.IllegalRewriteException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.ContactRequestDTO;
import com.ubuntu.ubuntu_app.model.dto.ContactRequestIdDTO;
import com.ubuntu.ubuntu_app.model.dto.ContactRequestStatusDTO;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessNameDTO;
import com.ubuntu.ubuntu_app.model.entities.ContactRequestEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Lazy
@RequiredArgsConstructor
@Service
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final MicrobusinessRepository microbusinessRepository;

    public ResponseEntity<?> createMessage(@Valid ContactRequestDTO requestMessage, Long id) {
        var microEntity = microbusinessRepository.findById(id);
        if (microEntity.isPresent()) {
            var foundMicro = microEntity.get();
            ContactRequestEntity request = new ContactRequestEntity(requestMessage, foundMicro);
            contactRequestRepository.save(request);
            return ResponseEntity.ok().body(ResponseMap.createResponse("Solicitud de contacto creada exitosamente"));
        } else {
            throw new SqlEmptyResponse("Falló al buscar emprendimiento con ese id");
        }
    }

    public ResponseEntity<?> getReviewedMessages() {
        var listOfReviewed = contactRequestRepository.findByReviewedTrue();
        if (listOfReviewed.isEmpty()) {
            throw new SqlEmptyResponse("No se encontraron solicitudes de contacto gestionadas");
        }
        var responseDTO = convertTest(listOfReviewed);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<?> getNonReviewedMessages() {
        var listOfUnreviewed = contactRequestRepository.findByReviewedFalse();
        if (listOfUnreviewed.isEmpty()) {
            throw new SqlEmptyResponse("No se encontraron solicitudes de contacto sin gestionar");
        }
        var responseDTO = convertTest(listOfUnreviewed);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<?> updateReview(Long id) {
        var contactEntity = contactRequestRepository.findById(id);
        if (!contactEntity.isPresent()) {
            throw new SqlEmptyResponse("La solicitud de contacto no existe");
        }
        var obtainedContactRequest = contactEntity.get();
        if (obtainedContactRequest.isReviewed()) {
            throw new IllegalRewriteException("La solicitud de contacto no se puede volver a cambiar a gestionado");
        } else {
            obtainedContactRequest.setReviewed(true);
        }
        return ResponseEntity.ok(ResponseMap.createResponse("Se actualizó el estado a gestionado"));
    }

    public ResponseEntity<?> findContact(Long id) {
        var contactEntity = contactRequestRepository.findById(id);
        if (!contactEntity.isPresent()) {
            throw new SqlEmptyResponse("La solicitud de contacto no existe");
        }
        var jsonResponse = new ContactRequestIdDTO(contactEntity.get());
        return ResponseEntity.ok(jsonResponse);
    }

    public List<?> convertTest(List<ContactRequestEntity> listStatus) {
        return listStatus.stream()
                .map(contactRequest -> new ContactRequestStatusDTO(
                        new MicrobusinessNameDTO(contactRequest.getMicrobusiness()), contactRequest))
                .toList();
    }

    public ResponseEntity<?> findByStatistics() {
        Map<String, Long> listOfContactRequest = new LinkedHashMap<>();
        var reviewed = contactRequestRepository.findByStatisticsContact(true, GlobalDate.getCurrentMonth(),
                GlobalDate.getCurrentYear());
        var unreviewed = contactRequestRepository.findByStatisticsContact(false, GlobalDate.getCurrentMonth(),
                GlobalDate.getCurrentYear());
        listOfContactRequest.put("Reviewed", reviewed);
        listOfContactRequest.put("Unreviewed", unreviewed);
        return ResponseEntity.ok(ResponseMap.responseGeneric("Found", listOfContactRequest));
    }

}
