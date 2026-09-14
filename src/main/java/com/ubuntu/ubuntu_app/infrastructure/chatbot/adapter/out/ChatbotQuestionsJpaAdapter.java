package com.ubuntu.ubuntu_app.infrastructure.chatbot.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.chatbot.port.out.ChatbotQuestionsRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.repository.ChatbotQuestionsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatbotQuestionsJpaAdapter implements ChatbotQuestionsRepositoryPort {

    private final ChatbotQuestionsRepository repository;

    @Override
    public List<ChatbotQuestionEntity> findQuestionsByCategory(String category) {
        return repository.findQuestionsByCategory(category);
    }

    @Override
    public Optional<ChatbotQuestionEntity> findByIdAndCategory(Long id) {
        return repository.findByIdAndCategory(id);
    }

    @Override
    public Optional<Long> findFKofAnswers(Long id) {
        return repository.findFKofAnswers(id);
    }
}
