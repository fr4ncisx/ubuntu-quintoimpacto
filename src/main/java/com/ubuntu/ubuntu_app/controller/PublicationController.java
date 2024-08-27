package com.ubuntu.ubuntu_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ubuntu.ubuntu_app.model.dto.PublicationRequestDTO;
import com.ubuntu.ubuntu_app.service.PublicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Publicaciones")
@RestController
@RequestMapping("/publications")
public class PublicationController {

    private final PublicationService publicationService;

    @PostMapping("/create")
    public ResponseEntity<?> createPublication(@Valid @RequestBody PublicationRequestDTO publicationsDTO) {
        return publicationService.create(publicationsDTO);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updatePublication(@Valid @RequestBody PublicationRequestDTO publicationsDTO,
            @RequestParam Long id) {
        return publicationService.update(publicationsDTO, id);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findPublicationById(@RequestParam Long id) {
        return publicationService.findById(id);
    }

    @PutMapping("/disable")
    public ResponseEntity<?> disablePublication(@RequestParam Long id) {
        return publicationService.disablePublication(id);
    }

    @PutMapping("/click")
    public ResponseEntity<?> newVisualization(@RequestParam Long id) {
        return publicationService.viewed(id);
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> publicationStatistics(@RequestParam Long limitSize) {
        return publicationService.getStatistics(limitSize);
    }

    @Operation(summary = "Buscar publicaciones activas o inactivas", description = "Ingresar parametro active=true (Publicaciones activas), active=false (Publicaciones inactivas)")
    @GetMapping("/find-all")
    public ResponseEntity<?> findAllPublicationsOrdered(@RequestParam boolean active) {
            return publicationService.findAllPublications(active);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPublication(@RequestParam String publication) {
            return publicationService.findPublication(publication);
    }
}
