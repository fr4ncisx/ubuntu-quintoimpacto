package com.ubuntu.ubuntu_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ubuntu.ubuntu_app.model.dto.UserDto;
import com.ubuntu.ubuntu_app.model.dto.UserUpdateDTO;
import com.ubuntu.ubuntu_app.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/user")
public class UserController {

        @Autowired
        private UserService userService;

        @Transactional
        @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario con sus respectivos datos")
        @ApiResponse(responseCode = "201", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Estado\": \"Creado exitosamente\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"campo\": \"nombre del campo\",\n" + //
                        "\"error\": \"no debe estar vacío\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "409", description = "Fallo al ingresar datos por error de sintaxis", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"error\": \"Error de formato en la solicitud\"\n" + //
                        "}")))
        @PostMapping("/register")
        public ResponseEntity<?> createNewUser(@RequestBody @Valid UserDto userDto) {
                return userService.registerUser(userDto);
        }

        @Transactional
        @Operation(summary = "Desactivar usuario", description = "Desactivar usuario que este previamente activado")
        @ApiResponse(responseCode = "202", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Estado\": \"Usuario desactivado exitosamente\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"campo\": \"nombre del campo\",\n" + //
                        "\"error\": \"no debe estar vacío\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Error\": \"El usuario no existe en la base de datos\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "409", description = "El usuario ya está desactivado", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Estado\": \"El usuario no se puede desactivar porque ya está desactivado\"\n" + //
                        "}")))
        @PutMapping("/deactivate")
        public ResponseEntity<?> deactivateUser(@RequestParam Long id) {
                return userService.deactivateUser(id);
        }

        @Transactional
        @Operation(summary = "Editar usuario", description = "Editar usuario")
        @ApiResponse(responseCode = "202", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Estado\": \"Usuario Modificado exitosamente\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"campo\": \"nombre del campo\",\n" + //
                        "\"error\": \"no debe estar vacío\"\n" + //
                        "}")))
        @ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos", content = @Content(schema = @Schema(defaultValue = "{\n"
                        + //
                        "\"Error\": \"El usuario no existe en la base de datos\"\n" + //
                        "}")))
        @PutMapping("/update")
        public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO userUpdate, @RequestParam Long id) {
                return userService.updateUser(userUpdate, id);
        }

        @Operation(summary = "Buscar todos los usuarios", security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = "[\r\n" + //
                                "\t{\r\n" + //
                                "\t\t\"id\": 1,\r\n" + //
                                "\t\t\"nombre\": \"Ubuntu\",\r\n" + //
                                "\t\t\"apellido\": \"Administración\",\r\n" + //
                                "\t\t\"email\": \"semilleroubuntu.dev@gmail.com\",\r\n" + //
                                "\t\t\"activo\": true,\r\n" + //
                                "\t\t\"rol\": \"ADMIN\",\r\n" + //
                                "\t\t\"telefono\": \"+54 9 424 4525 4638\"\r\n" + //
                                "\t},\r\n" + //
                                "\t{\r\n" + //
                                "\t\t\"id\": 2,\r\n" + //
                                "\t\t\"nombre\": \"Francisco\",\r\n" + //
                                "\t\t\"apellido\": \"Montoro\",\r\n" + //
                                "\t\t\"email\": \"f.montoro@gmail.com\",\r\n" + //
                                "\t\t\"activo\": true,\r\n" + //
                                "\t\t\"rol\": \"ADMIN\",\r\n" + //
                                "\t\t\"telefono\": \"+54 9 351 666674\"\r\n" + //
                                "\t}\r\n" + //
                                "]")))
        @GetMapping("/fetch")
        public ResponseEntity<?> getAllUsers() {
                return userService.findAllUsers();
        }
}
