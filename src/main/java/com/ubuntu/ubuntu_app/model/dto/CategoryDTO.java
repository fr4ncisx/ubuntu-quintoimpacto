package com.ubuntu.ubuntu_app.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CategoryDTO{
        @NotBlank(message = "El nombre no debe estar vacio") private String nombre;
}
        


