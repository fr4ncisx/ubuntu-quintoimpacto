package com.ubuntu.ubuntu_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ubuntu.ubuntu_app.model.dto.ContactRequestDTO;
import com.ubuntu.ubuntu_app.service.ContactRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Mensajes de contacto")
@RestController
@RequestMapping("/contact")
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    @ApiResponse(responseCode = "200", description = "Solicitud creada", content = @Content(schema = @Schema(defaultValue = "{\r\n"
            + //
            "\t\"Estado\": \"Solicitud de contacto creada exitosamente\"\r\n" + //
            "}")))
    @ApiResponse(responseCode = "404", description = "Microemprendimiento no encontrado", content = @Content(schema = @Schema(defaultValue = "{\r\n"
            + //
            "\t\"Error\": \"Falló al buscar emprendimiento con ese id\"\r\n" + //
            "}")))
    @Operation(summary = "Crear solicitud de contacto", description = "Crea una nueva solicitud de contacto, recibe como parámetro el id del microemprendimiento")
    @Transactional(readOnly = false)
    @PostMapping("/new-request")
    public ResponseEntity<?> newContactMessage(@RequestBody @Valid ContactRequestDTO requestMessage,
            @RequestParam Long id) {
        return contactRequestService.createMessage(requestMessage, id);
    }

    @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(defaultValue = "[\r\n" + //
            "\t{\r\n" + //
            "\t\t\"microemprendimiento\": {\r\n" + //
            "\t\t\t\"id\": 1,\r\n" + //
            "\t\t\t\"nombre\": \"Ciclo Verde\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"id\": 2,\r\n" + //
            "\t\t\"fecha_solicitud\": \"2024-07-31\",\r\n" + //
            "\t\t\"nombre\": \"Gregorio Santos\",\r\n" + //
            "\t\t\"email\": \"gregorio.santos@gmail.com\",\r\n" + //
            "\t\t\"telefono\": \"+54 9 342 544895\",\r\n" + //
            "\t\t\"mensaje\": \"Hola quisiera obtener más información acerca de éste emprendimiento\"\r\n" + //
            "\t}\r\n" + //
            "]")))
    @ApiResponse(responseCode = "404", description = "No hay solicitudes de contacto gestionadas", content = @Content(schema = @Schema(defaultValue = "{\r\n"
            + //
            "\t\"Error\": \"No se encontraron solicitudes de contacto gestionadas\"\r\n" + //
            "}")))
    @Operation(summary = "Obtiene todas las solicitudes gestionadas", description = "Devuelve una lista de solicitudes gestionadas")
    @Transactional(readOnly = true)
    @GetMapping("/reviewed")
    public ResponseEntity<?> obtainReviewedContactMessages() {
        return contactRequestService.getReviewedMessages();
    }

    @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(defaultValue = "[\r\n" + //
            "\t{\r\n" + //
            "\t\t\"microemprendimiento\": {\r\n" + //
            "\t\t\t\"id\": 1,\r\n" + //
            "\t\t\t\"nombre\": \"Ciclo Verde\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"id\": 2,\r\n" + //
            "\t\t\"fecha_solicitud\": \"2024-07-31\",\r\n" + //
            "\t\t\"nombre\": \"Gregorio Santos\",\r\n" + //
            "\t\t\"email\": \"gregorio.santos@gmail.com\",\r\n" + //
            "\t\t\"telefono\": \"+54 9 342 544895\",\r\n" + //
            "\t\t\"mensaje\": \"Hola quisiera obtener más información acerca de éste emprendimiento\"\r\n" + //
            "\t}\r\n" + //
            "]")))
    @ApiResponse(responseCode = "404", description = "No hay solicitudes de contacto sin gestionar", content = @Content(schema = @Schema(defaultValue = "{\r\n"
            + //
            "\t\"Error\": \"No se encontraron solicitudes de contacto sin gestionar\"\r\n" + //
            "}")))
    @Operation(summary = "Obtiene todas las solicitudes no gestionadas", description = "Devuelve una lista de solicitudes no gestionadas")
    @Transactional(readOnly = true)
    @GetMapping("/unreviewed")
    public ResponseEntity<?> obtainUnreviewedContactMessages() {
        return contactRequestService.getNonReviewedMessages();

    }

    @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(defaultValue = "{\r\n" + //
            "\t\"Estado\": \"Se actualizó el estado a gestionado\"\r\n" + //
            "}")))
    @ApiResponse(responseCode = "404", description = "La solicitud de contacto no existe en la base de datos", content = @Content(schema = @Schema(defaultValue = "{\r\n"
            + //
            "\t\"Error\": \"La solicitud de contacto no existe\"\r\n" + //
            "}")))
    @ApiResponse(responseCode = "400", description = "No se puede cambiar una solicitud gestionada a gestionada", content = @Content(schema = @Schema(defaultValue = "{\r\n"
            + //
            "\t\"Error\": \"La solicitud de contacto no se puede volver a cambiar a gestionado\"\r\n" + //
            "}")))
    @Operation(summary = "Cambiar solicitud a gestionada", description = "Cambia una solicitud de contacto no gestionada a gestionada")
    @Transactional(readOnly = false)
    @PutMapping("/update")
    public ResponseEntity<?> updateReviewStatus(@RequestParam Long id) {
        return contactRequestService.updateReview(id);
    }

    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(defaultValue = "{\r\n" + //
            "\t\"microemprendimiento\": {\r\n" + //
            "\t\t\"id\": 1,\r\n" + //
            "\t\t\"nombre\": \"Ciclo Verde\"\r\n" + //
            "\t},\r\n" + //
            "\t\"id\": 2,\r\n" + //
            "\t\"fecha_solicitud\": \"2024-07-31\",\r\n" + //
            "\t\"nombre\": \"Gregorio Santos\",\r\n" + //
            "\t\"email\": \"gregorio.santos@gmail.com\",\r\n" + //
            "\t\"telefono\": \"+54 9 342 544895\",\r\n" + //
            "\t\"mensaje\": \"Hola quisiera obtener más información acerca de éste emprendimiento\",\r\n" + //
            "\t\"gestionado\": true\r\n" + //
            "}")))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(defaultValue = "{\r\n" + //
            "\t\"Error\": \"La solicitud de contacto no existe\"\r\n" + //
            "}")))
    @Operation(summary = "Busca mensaje de contacto por id", description = "Devuelve un mensaje de contacto con todos sus atributos")
    @GetMapping("/find")
    public ResponseEntity<?> findContactRequest(@RequestParam Long id) {
        return contactRequestService.findContact(id);
    }

    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(defaultValue = "{\r\n" + //
                        "\t\"Found\": {\r\n" + //
                        "\t\t\"Reviewed\": 0,\r\n" + //
                        "\t\t\"Unreviewed\": 0\r\n" + //
                        "\t}\r\n" + //
                        "}")))
    @Operation(summary = "Solicitudes de contacto estadisticas por mes actual")
    @GetMapping("/statistics/find")
    public ResponseEntity<?> contactRequestStatisticsMonth() {
            return contactRequestService.findByStatistics();
    }
}
