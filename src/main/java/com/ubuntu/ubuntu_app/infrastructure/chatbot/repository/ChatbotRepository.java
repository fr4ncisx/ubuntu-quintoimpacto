package com.ubuntu.ubuntu_app.infrastructure.chatbot.repository;

import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotRepository extends JpaRepository<ChatbotResponseEntity,Long> {   
    
}
