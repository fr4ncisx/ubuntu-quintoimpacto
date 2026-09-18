package com.ubuntu.ubuntu_app.infrastructure.contact.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.contact.port.in.ContactRequestUseCase;
import com.ubuntu.ubuntu_app.application.contact.api.CreateContactRequest;
import com.ubuntu.ubuntu_app.application.contact.api.ContactRequestResponse;
import com.ubuntu.ubuntu_app.application.contact.api.ContactRequestSummary;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.api.Pages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact-requests")
@RequiredArgsConstructor
public class ContactRequestV1Controller {

    private final ContactRequestUseCase contactRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> createContactRequest(
            @RequestBody @Valid CreateContactRequest request, @RequestParam Long microId) {
        contactRequestService.create(request, microId);
        return new ResponseEntity<>(ApiResponse.created(
                Map.of("Estado", "Solicitud de contacto creada exitosamente")), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ContactRequestSummary>>>> listByReviewStatus(
            @RequestParam boolean reviewed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<ContactRequestSummary> assembler) {
        var pageable = Pages.of(page, size, Sort.by("id"));
        if (reviewed) {
            return ResponseEntity.ok(ApiResponse.ok(assembler.toModel(contactRequestService.findReviewed(pageable))));
        }
        return ResponseEntity.ok(ApiResponse.ok(assembler.toModel(contactRequestService.findUnreviewed(pageable))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactRequestResponse>> findContactRequest(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(contactRequestService.findById(id)));
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<ApiResponse<Map<String, String>>> markAsReviewed(@PathVariable Long id) {
        contactRequestService.markReviewed(id);
        return ResponseEntity.ok(
                ApiResponse.message("Se actualizó el estado a gestionado", HttpStatus.OK));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Map<String, Long>>>> statistics() {
        return ResponseEntity.ok(ApiResponse.ok(
                Map.of("Found", contactRequestService.countByStatistics())));
    }
}
