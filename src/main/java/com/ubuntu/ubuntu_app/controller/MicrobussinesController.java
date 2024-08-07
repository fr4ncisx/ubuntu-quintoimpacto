package com.ubuntu.ubuntu_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ubuntu.ubuntu_app.infra.statuses.MicroResponseDoc;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessDTO;
import com.ubuntu.ubuntu_app.service.MicrobusinessService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Tag(name = "Microemprendimiento")
@RestController
@RequestMapping("/micro")
public class MicrobussinesController {

        @Autowired
        MicrobusinessService microbusinessService;

        @PostMapping("/new")
        @Transactional
        @Operation(summary = "Crear microemprendimiento", description = "Crea un nuevo microemprendimiento con sus respectivos datos")
        @ApiResponse(responseCode = "201", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_created)))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_edit_validation_error)))
        @ApiResponse(responseCode = "409", description = "Fallo al ingresar datos por error de sintaxis", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_format_error)))
        public ResponseEntity<?> create(@RequestBody @Valid MicrobusinessDTO microbusinessDTO) {
                return microbusinessService.create(microbusinessDTO);
        }

        @PutMapping("/edit")
        @Transactional
        @Operation(summary = "Editar microemprendimiento", description = "Editar microemprendimiento")
        @ApiResponse(responseCode = "202", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_edit_ok)))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_edit_validation_error)))
        @ApiResponse(responseCode = "404", description = "No se encontró el microemprendimiento", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_error_notFound)))
        public ResponseEntity<?> update(@RequestBody @Valid MicrobusinessDTO microbusinessDTO, @RequestParam Long id) {
                return microbusinessService.update(microbusinessDTO, id);
        }

        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_found_searchbar)))
        @ApiResponse(responseCode = "404", description = "No se encontró el microemprendimiento", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_not_found_searchbar)))
        @ApiResponse(responseCode = "409", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_validation_error)))
        @Operation(summary = "Buscar microemprendimiento", description = "Devuelve microemprendimientos que coincidan con la/las primeras letras o el nombre completo")
        @GetMapping("/find")
        public ResponseEntity<?> findMicroBussiness(@RequestParam String name) {
                return microbusinessService.findByName(name);
        }

        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_found_category)))
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_error)))
        @Operation(summary = "Buscar microemprendimientos", description = "Devuelve microemprendimientos filtrados por la categoria ingresada")
        @GetMapping("/find/category")
        public ResponseEntity<?> findAllMicrobussiness(@RequestParam String name) {
                return microbusinessService.findByCategory(name);
        }

        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_hide_ok)))
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_not_found_one)))
        @Operation(summary = "Ocultar emprendimiento")
        @PutMapping("/hide")
        public ResponseEntity<?> hideMicroBussiness(@RequestParam Long id) {
                return microbusinessService.hideMicro(id);
        }

        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_deleted)))
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_not_found_one)))
        @Operation(summary = "Eliminar emprendimiento de la base de datos")
        @DeleteMapping("/delete")
        public ResponseEntity<?> deleteMcroBussines(@RequestParam Long id) {
                return microbusinessService.deleteMicro(id);
        }

        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_found)))
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(defaultValue = MicroResponseDoc.micro_not_found_one)))
        @Operation(summary = "Buscar todos los microemprendimientos", description = "Devuelve microemprendimientos todos los microemprendimientos")
        @GetMapping("/api/find-all")
        public ResponseEntity<?> findAllMicrobussiness() {
                return microbusinessService.getAllMicro();
        }
}
