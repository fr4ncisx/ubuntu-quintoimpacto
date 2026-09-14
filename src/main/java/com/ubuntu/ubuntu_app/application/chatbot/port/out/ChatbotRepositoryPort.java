package com.ubuntu.ubuntu_app.application.chatbot.port.out;

import java.util.List;
import java.util.Optional;

import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;

public interface ChatbotRepositoryPort {

    List<ChatbotResponseEntity> findAll();

    Optional<ChatbotResponseEntity> findById(Long id);
}
