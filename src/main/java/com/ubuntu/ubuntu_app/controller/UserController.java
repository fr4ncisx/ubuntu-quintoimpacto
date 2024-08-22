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

import com.ubuntu.ubuntu_app.infra.statuses.UserResponseDoc;
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
        @ApiResponse(responseCode = "201", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_register_ok)))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_validation_error)))
        @ApiResponse(responseCode = "409", description = "Fallo al ingresar datos por error de sintaxis", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_format_error)))
        @PostMapping("/register")
        public ResponseEntity<?> createNewUser(@RequestBody @Valid UserDto userDto) {
                return userService.registerUser(userDto);
        }

        @Transactional
        @Operation(summary = "Desactivar usuario", description = "Desactivar usuario que este previamente activado")
        @ApiResponse(responseCode = "202", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_deactivated)))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_validation_error)))
        @ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_not_found)))
        @ApiResponse(responseCode = "409", description = "El usuario ya está desactivado", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_already_deactivated)))
        @PutMapping("/deactivate")
        public ResponseEntity<?> deactivateUser(@RequestParam Long id) {
                return userService.deactivateUser(id);
        }

        @Transactional
        @Operation(summary = "Editar usuario", description = "Editar usuario")
        @ApiResponse(responseCode = "202", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_modified)))
        @ApiResponse(responseCode = "400", description = "Fallo al ingresar datos por campos vacios", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_validation_error)))
        @ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_not_found)))
        @PutMapping("/update")
        public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO userUpdate, @RequestParam Long id) {
                return userService.updateUser(userUpdate, id);
        }

        @Operation(summary = "Buscar todos los usuarios", security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponse(responseCode = "200", description = "Respuesta operación válida", content = @Content(schema = @Schema(defaultValue = UserResponseDoc.user_fetch)))
        @GetMapping("/fetch")
        public ResponseEntity<?> getAllUsers() {
                return userService.findAllUsers();
        }
}
