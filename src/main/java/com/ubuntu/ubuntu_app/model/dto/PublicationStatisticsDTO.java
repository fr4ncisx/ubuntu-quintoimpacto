package com.ubuntu.ubuntu_app.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationStatisticsDTO {
    private String title;
    private LocalDate date;
    private Long visualizations;
  
}
