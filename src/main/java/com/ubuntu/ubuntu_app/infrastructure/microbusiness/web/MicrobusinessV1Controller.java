package com.ubuntu.ubuntu_app.infrastructure.microbusiness.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.microbusiness.port.in.MicrobusinessUseCase;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessCategorySummary;
import com.ubuntu.ubuntu_app.application.microbusiness.api.CreateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.UpdateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.NearbyMicrobusinessResponse;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessSummary;
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
@RequestMapping("/api/v1/microbusiness")
@RequiredArgsConstructor
public class MicrobusinessV1Controller {

    private final MicrobusinessUseCase microbusinessService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> create(
            @RequestBody @Valid CreateMicrobusinessRequest request) {
        microbusinessService.create(request);
        return new ResponseEntity<>(ApiResponse.created(Map.of("Estado", "Creado exitosamente")),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> update(@PathVariable Long id,
            @RequestBody @Valid UpdateMicrobusinessRequest request) {
        microbusinessService.update(request, id);
        return ResponseEntity.ok(ApiResponse.message(
                "La edición del microemprendimiento fue correcta", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        microbusinessService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.noContent());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MicrobusinessSummary>>>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<MicrobusinessSummary> assembler) {
        Page<MicrobusinessSummary> result = microbusinessService.findByName(name,
                Pages.of(page, size, Sort.by("id")));
        return ResponseEntity.ok(ApiResponse.ok(assembler.toModel(result)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MicrobusinessCategorySummary>>>> findByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<MicrobusinessCategorySummary> assembler) {
        Page<MicrobusinessCategorySummary> result = microbusinessService.findAll(category,
                Pages.of(page, size, Sort.by("id")));
        return ResponseEntity.ok(ApiResponse.ok(assembler.toModel(result)));
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<ApiResponse<Map<String, String>>> setVisibility(@PathVariable Long id,
            @RequestParam boolean active) {
        microbusinessService.setVisibility(id, active);
        if (active) {
            return ResponseEntity.ok(
                    ApiResponse.message("El microemprendimiento fue activado", HttpStatus.OK));
        }
        return ResponseEntity.ok(
                ApiResponse.message("El microemprendimiento fue ocultado", HttpStatus.OK));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MicrobusinessSummary>>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<MicrobusinessSummary> assembler) {
        Page<MicrobusinessSummary> result = microbusinessService.findAllActive(
                Pages.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
        return ResponseEntity.ok(ApiResponse.ok(assembler.toModel(result)));
    }

    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MicrobusinessSummary>>>> findByStatus(
            @RequestParam boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<MicrobusinessSummary> assembler) {
        Page<MicrobusinessSummary> result = microbusinessService.findByActive(active,
                Pages.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
        return ResponseEntity.ok(ApiResponse.ok(assembler.toModel(result)));
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<ApiResponse<Map<String, Long>>> statisticsByMonth() {
        return ResponseEntity.ok(ApiResponse.ok(
                Map.of("Found", microbusinessService.countByCurrentMonth())));
    }

    @GetMapping("/statistics/by-category")
    public ResponseEntity<ApiResponse<Map<String, Map<String, Long>>>> statisticsByCategory() {
        return ResponseEntity.ok(ApiResponse.ok(
                Map.of("Found", microbusinessService.countByCategoryCurrentMonth())));
    }

    @GetMapping("/near")
    public ResponseEntity<ApiResponse<List<NearbyMicrobusinessResponse>>> findNear(
            @RequestParam double lat, @RequestParam double lon) {
        return ResponseEntity.ok(ApiResponse.ok(microbusinessService.findNearby(lat, lon)));
    }
}
