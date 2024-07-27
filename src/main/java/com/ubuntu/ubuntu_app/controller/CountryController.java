package com.ubuntu.ubuntu_app.controller;

import com.ubuntu.ubuntu_app.service.PaisService;
import com.ubuntu.ubuntu_app.service.ProvinciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Paises y provincias")
@RestController
public class CountryController {
    @Autowired
    private PaisService paisService;
    @Autowired
    private ProvinciaService provService;


    @ApiResponse(responseCode = "202", description = "provincias encontradas ", content = @Content(schema = @Schema(defaultValue = "[\n" + //
            "{\n" + //
            "\"nombre\": \"provincia_1\"\n" + //
            "},\n" + //
            "{\n" + //
            "\"nombre\": \"provincia_2\"\n" + //
            "},\n" + //
            "{\n" + //
            "\"nombre\": \"provincia_3\"\n" + //
            "},\n" + //
            "{\n" + //
            "\"nombre\": \"provincia_4\"\n" + //
            "}\n" + //
            "]")))
    @ApiResponse(responseCode = "404", description = "No hay provincias", content = @Content(schema = @Schema(defaultValue = "{\n" + //
            "\"Error\": \"provincias no encontradas\"\n" + //
            "}")))
    @ApiResponse(responseCode = "404", description = "No hay Paises", content = @Content(schema = @Schema(defaultValue = "{\n" + //
            "\"Error\": \"Pais no encontrado\"\n" + //
            "}")))
    @Operation(summary = "Buscar provincias", description = "Busca las provincias que esten asociadas al país ingresado como parámetro")
    @GetMapping("/provincias")
    public ResponseEntity<?> filteredProvinces(@RequestParam String pais) {
        return provService.findProvinceByCountry(pais);
    }


    @ApiResponse(responseCode = "202", description = "Paises encontrados ", content = @Content(schema = @Schema(defaultValue = "[\n" + //
            "{\n" + //
            "\"nombre\": \"pais_1\"\n" + //
            "},\n" + //
            "{\n" + //
            "\"nombre\": \"pais_2\"\n" + //
            "},\n" + //
            "{\n" + //
            "\"nombre\": \"pais_3\"\n" + //
            "},\n" + //
            "{\n" + //
            "\"nombre\": \"pais_4\"\n" + //
            "}\n" + //
            "]")))
    @ApiResponse(responseCode = "404", description = "No hay Paises", content = @Content(schema = @Schema(defaultValue = "{\n" + //
            "\"Error\": \"No hay Paises\"\n" + //
            "}")))    
    @Operation(summary = "Buscar paises", description = "Devuelve todos los paises existentes en la base de datos o devuelve un mensaje que no hay paises en la base de datos")
    @GetMapping("/paises")
    public ResponseEntity<?> findAllCountries() {
        return paisService.getAll();
    }

}
