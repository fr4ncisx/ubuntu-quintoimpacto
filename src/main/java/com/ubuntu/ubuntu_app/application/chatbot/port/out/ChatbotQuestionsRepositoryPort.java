package com.ubuntu.ubuntu_app.application.chatbot.port.out;

import java.util.List;
import java.util.Optional;

import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;

public interface ChatbotQuestionsRepositoryPort {

    List<ChatbotQuestionEntity> findQuestionsByCategory(String category);

    Optional<ChatbotQuestionEntity> findByIdAndCategory(Long id);

    Optional<Long> findFKofAnswers(Long id);
}
