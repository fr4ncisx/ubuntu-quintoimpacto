package com.ubuntu.ubuntu_app.application.chatbot.port.in;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;

public interface ChatBotUseCase {

    List<ChatbotQuestionResponse> findQuestionsByCategory(String category);

    Object findAnswerByQuestionId(Long id);

    Map<String, ?> answer(String question);
}
