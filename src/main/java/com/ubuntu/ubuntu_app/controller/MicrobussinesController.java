package com.ubuntu.ubuntu_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        @ApiResponse(responseCode = "201", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Estado\": \"Creado exitosamente\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"campo\": \"nombre del campo\",\n" + //
                        "\"error\": \"no debe estar vacío\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "409", description = "Fallo al ingresar datos por error de sintaxis", content = @Content(schema = @Schema(defaultValue = "{\r\n"
                        + //
                        "\"error\": \"Error de formato en la solicitud\"\n" + //
                        "}")))
        public ResponseEntity<?> create(@RequestBody @Valid MicrobusinessDTO microbusinessDTO) {
                return microbusinessService.create(microbusinessDTO);
        }

        @PutMapping("/edit")
        @Transactional
        @Operation(summary = "Editar microemprendimiento", description = "Editar microemprendimiento")
        @ApiResponse(responseCode = "202", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Estado\": \"La edición del microemprendimiento fue correcta\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"campo\": \"nombre del campo\",\n" + //
                        "\"error\": \"no debe estar vacío\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "404", description = "No se encontró el microemprendimiento", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Error\": \"Microemprendimiento no existe en la base de datos\"\n" + //
                        "}")))
        public ResponseEntity<?> update(@RequestBody @Valid MicrobusinessDTO microbusinessDTO, @RequestParam Long id) {
                return microbusinessService.update(microbusinessDTO, id);
        }

        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "[\n"
                        + //
                        "{\n" + //
                        "\t\"nombre\": \"Eco Emprendimiento\",\n" + //
                        "\t\"descripcion\": \"Descripción del emprendimiento\",\n" + //
                        "\t\"masInformacion\": \"Mas información emprendimiento\",\n" + //
                        "\t\"pais\": \"Argentina\",\n" + //
                        "\t\"provincia\": \"Santa Fe\",\n" + //
                        "\t\"ciudad\": \"Rosario\",\n" + //
                        "\t\"categoria\": {\n" + //
                        "\t\t\"nombre\": \"Agroecología/Orgánicos/Alimentación saludable\"\n" + //
                        "\t},\r\n" + //
                        "\t\"subcategoria\": \"{optional}\",\n" + //
                        "\t\"imagenes\": \"null\",\n" + //
                        "\t\"mensajeDeContacto\": \"null\",\n" + //
                        "\t}\n" + //
                        "]")))
        @ApiResponse(responseCode = "404", description = "No se encontró el microemprendimiento", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Error\": \"El nombre empezado por '{nombre_buscado}' no ha arrojado resultados\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "409", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Error\": \"El nombre no debe estar vacio\"\n" + //
                        "}")))
        @Operation(summary = "Buscar microemprendimiento", description = "Devuelve microemprendimientos que coincidan con la/las primeras letras o el nombre completo")
        @GetMapping("/find")
        public ResponseEntity<?> findMicroBussiness(@RequestParam String name) {
                return microbusinessService.findByName(name);
        }

        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "[\r\n"
                        + //
                        "\t{\r\n" + //
                        "\t\t\"nombre\": \"Impacto Positivo\",\r\n" + //
                        "\t\t\"descripcion\": \"Promovemos la inclusión financiera y el desarrollo local a través de microfinanzas.\",\r\n"
                        + //
                        "\t\t\"masInformacion\": \"Facilitamos el acceso a servicios financieros para comunidades desatendidas.\",\r\n"
                        + //
                        "\t\t\"pais\": \"Perú\",\r\n" + //
                        "\t\t\"provincia\": \"Lima\",\r\n" + //
                        "\t\t\"ciudad\": \"Lima\",\r\n" + //
                        "\t\t\"subcategoria\": \"Microfinanzas\",\r\n" + //
                        "\t\t\"imagenes\": \"null\"\r\n" + //
                        "\t},\r\n" + //
                        "\t{\r\n" + //
                        "\t\t\"nombre\": \"Crecimiento Sostenible\",\r\n" + //
                        "\t\t\"descripcion\": \"Promovemos el desarrollo local a través de proyectos de inclusión financiera.\",\r\n"
                        + //
                        "\t\t\"masInformacion\": \"Nuestros programas están diseñados para empoderar a comunidades a través de servicios financieros accesibles.\",\r\n"
                        + //
                        "\t\t\"pais\": \"Ecuador\",\r\n" + //
                        "\t\t\"provincia\": \"Pichincha\",\r\n" + //
                        "\t\t\"ciudad\": \"Quito\",\r\n" + //
                        "\t\t\"subcategoria\": \"Empoderamiento comunitario\",\r\n" + //
                        "\t\t\"imagenes\": \"null\"\r\n" + //
                        "\t}\r\n" + //
                        "]")))
        @ApiResponse(responseCode = "404", description = "No se encontró el microemprendimiento", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Error\": \"No se encontraron microemprendimientos\"\n" + //
                        "}")))
        @Operation(summary = "Buscar microemprendimientos", description = "Devuelve microemprendimientos filtrados por la categoria ingresada")
        @GetMapping("/find/category")
        public ResponseEntity<?> findAllMicrobussiness(@RequestParam String name) {
                return microbusinessService.findByCategory(name);
        }

        @PutMapping("/hide")
        public ResponseEntity<?> hideMicroBussiness(@RequestParam Long id) {
                return microbusinessService.hideMicro(id);
        }

        @DeleteMapping("/delete")
        public ResponseEntity<?> deleteMcroBussines(@RequestParam Long id) {
                return microbusinessService.deleteMicro(id);
        }
        @GetMapping("/api/find-all")
        public ResponseEntity<?> findAllMicrobussiness() {
                return microbusinessService.getAllMicro();
        }
}
