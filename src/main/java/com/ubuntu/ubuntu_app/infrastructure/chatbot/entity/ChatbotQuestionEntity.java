package com.ubuntu.ubuntu_app.infrastructure.chatbot.entity;

import java.util.Objects;

import com.ubuntu.ubuntu_app.application.chatbot.ChatbotCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
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

    public ChatbotQuestionEntity() {
    }

    public ChatbotQuestionEntity(Long id, String question, ChatbotCategory category) {
        this.id = id;
        this.question = question;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ChatbotCategory getCategory() {
        return category;
    }

    public void setCategory(ChatbotCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatbotQuestionEntity that = (ChatbotQuestionEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChatbotQuestionEntity{id=" + id + ", question='" + question + "', category=" + category + "}";
    }
}
