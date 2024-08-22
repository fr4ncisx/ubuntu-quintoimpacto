package com.ubuntu.ubuntu_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatbotCategoriesDTO {
    private Long id;
    private String question;
}
