package com.ubuntu.ubuntu_app.infrastructure.publication.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.publication.port.in.PublicationUseCase;
import com.ubuntu.ubuntu_app.application.publication.api.PublicationResponse;
import com.ubuntu.ubuntu_app.application.publication.api.CreatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.UpdatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.PublicationStatistics;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.api.Pages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publications")
@RequiredArgsConstructor
public class PublicationV1Controller {

    private final PublicationUseCase publicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> createPublication(
            @Valid @RequestBody CreatePublicationRequest request) {
        publicationService.create(request);
        return new ResponseEntity<>(ApiResponse.created(
                Map.of("Estado", "Publicación creada")), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublicationResponse>> findPublicationById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(publicationService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> updatePublication(@PathVariable Long id,
            @Valid @RequestBody UpdatePublicationRequest request) {
        publicationService.update(request, id);
        return ResponseEntity.ok(ApiResponse.message("Updated succesfully", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePublication(@PathVariable Long id) {
        publicationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.noContent());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PublicationResponse>>> findAllPublications(
            @RequestParam boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(publicationService.findAll(active,
                Pages.of(page, size, Sort.by(Sort.Direction.DESC, "date")))));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PublicationResponse>>> searchPublications(@RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(publicationService.search(q,
                Pages.of(page, size, Sort.by("id")))));
    }

    @PostMapping("/{id}/views")
    public ResponseEntity<ApiResponse<Map<String, String>>> newVisualization(@PathVariable Long id) {
        publicationService.registerView(id);
        return ResponseEntity.ok(ApiResponse.message("Added new visualization", HttpStatus.OK));
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<ApiResponse<Map<String, String>>> setVisibility(@PathVariable Long id,
            @RequestParam boolean active) {
        publicationService.setVisibility(id, active);
        if (active) {
            return ResponseEntity.ok(ApiResponse.message("Publication reactivated", HttpStatus.OK));
        }
        return ResponseEntity.ok(ApiResponse.message("Hidden publication sucessfully", HttpStatus.OK));
    }

    @GetMapping("/statistics/top")
    public ResponseEntity<ApiResponse<List<PublicationStatistics>>> publicationStatistics(
            @RequestParam Long limit) {
        return ResponseEntity.ok(ApiResponse.ok(publicationService.getStatistics(limit)));
    }
}
