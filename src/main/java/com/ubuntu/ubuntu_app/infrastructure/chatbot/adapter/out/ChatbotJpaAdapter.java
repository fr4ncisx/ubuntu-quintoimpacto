package com.ubuntu.ubuntu_app.infrastructure.chatbot.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.chatbot.port.out.ChatbotRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.repository.ChatbotRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatbotJpaAdapter implements ChatbotRepositoryPort {

    private final ChatbotRepository repository;

    @Override
    public List<ChatbotResponseEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ChatbotResponseEntity> findAllWithQuestions() {
        return repository.findAllWithQuestions();
    }

    @Override
    public Optional<ChatbotResponseEntity> findById(Long id) {
        return repository.findById(id);
    }
}
