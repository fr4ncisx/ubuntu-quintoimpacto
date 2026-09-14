package com.ubuntu.ubuntu_app.infrastructure.chatbot.entity;

import com.ubuntu.ubuntu_app.application.chatbot.ChatbotCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chatbot_questions")
public class ChatbotQuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 400)
    private String question;
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = true)
    private ChatbotCategory category;   
}
