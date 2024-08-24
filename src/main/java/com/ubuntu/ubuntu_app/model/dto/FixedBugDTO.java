package com.ubuntu.ubuntu_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"description", "category", "creation_date", "fixed_date"})
public class FixedBugDTO {
    private String description;
    private String category;
    @JsonProperty(value = "creation_date")
    private String date;
    @JsonProperty(value = "fixed_date")
    private LocalDate fixedDate;
}
