package com.ubuntu.ubuntu_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnfixedBugDTO {
    private Long id;
    private String description;
    private String category;
    private LocalDate date;
}
