package com.ubuntu.ubuntu_app.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private List<ImageDTO> imagenes;
}
