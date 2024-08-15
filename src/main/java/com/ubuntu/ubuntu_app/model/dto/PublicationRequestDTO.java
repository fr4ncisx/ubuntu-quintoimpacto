package com.ubuntu.ubuntu_app.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationRequestDTO {
    @NotBlank
    @NotNull
    @Size(max = 200)
    private String title;
    @NotBlank
    @NotNull
    @Size(max = 2000)
    private String description;
    @NotNull
    private List<ImageDTO> imagenes;
}
