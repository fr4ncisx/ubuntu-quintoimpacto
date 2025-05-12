package com.ubuntu.ubuntu_app.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO{
        @NotBlank(message = "El nombre no debe estar vacio") private String nombre;
}
        


