package com.ubuntu.ubuntu_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubuntu.ubuntu_app.model.dto.CategoryDTO;
import com.ubuntu.ubuntu_app.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiResponse(responseCode = "201", description = "Categoría creada", content = @Content(schema = @Schema(defaultValue = "{\r\n" + //
                "\"Estado\": \"Categoría creada exitosamente\"\n" + //
                "}")))
                @ApiResponse(responseCode = "400", description = "Error al ingresar categoría", content = @Content(schema = @Schema(defaultValue = "[\r\n" + //
                                        "\t{\n" + //
                                        "\t\"campo\": \"nombre\",\n" + //
                                        "\t\"error\": \"El nombre no debe estar vacio\"\n" + //
                                        "\t}\n" + //
                                        "]")))
    @ApiResponse(responseCode = "409", description = "Json body vacio ", content = @Content(schema = @Schema(defaultValue = " {\n" +
            "    \"error\": \"Error body vacio\"\n" +
            "}")))

    @PostMapping("/new")
    @Operation(summary = "Crear categoría", description = "Se crea una categoria en la base de datos")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return categoryService.createCategory(categoryDTO);
    }

    @ApiResponse(responseCode = "202", description = "Categoría/as encontrada/as ", content = @Content(schema = @Schema(defaultValue = "[\n" + //
                "{\n" + //
                "\"nombre\": \"categoria_1\"\n" + //
                "},\n" + //
                "{\n" + //
                "\"nombre\": \"categoria_2\"\n" + //
                "},\n" + //
                "{\n" + //
                "\"nombre\": \"categoria_3\"\n" + //
                "},\n" + //
                "{\n" + //
                "\"nombre\": \"categoria_4\"\n" + //
                "}\n" + //
                "]")))
    @ApiResponse(responseCode = "404", description = "No existen categorías", content = @Content(schema = @Schema(defaultValue = "{" + //
                "\"Error\": \"No se encontraron categorías en la base de datos\"\n" + //
                "}")))
    @GetMapping("/search")
    @Operation(summary = "Buscar categorias", description = "Devuelve dos estados, una lista de categorias existentes en la base de datos o no hay categorías")
    public ResponseEntity<?> findAllCategories(){
        return categoryService.getAllCategories();
    }
}
