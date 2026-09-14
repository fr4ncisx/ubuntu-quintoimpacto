package com.ubuntu.ubuntu_app.infrastructure.chatbot.adapter.mapper;

import org.mapstruct.Mapper;

import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;

@Mapper(componentModel = "spring")
public interface ChatbotMapper {

    ChatbotQuestionResponse toQuestionResponse(ChatbotQuestionEntity entity);
}
