package com.ubuntu.ubuntu_app.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"possibleQuestion, answer, distance"})
public class AnswerThreshold {
    private String possibleQuestion;
    private String answer;
    private Double distance;

}
