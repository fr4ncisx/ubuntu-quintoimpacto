package com.ubuntu.ubuntu_app.infrastructure.chatbot.repository;

import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotRepository extends JpaRepository<ChatbotResponseEntity,Long> {

    @Query("SELECT r FROM ChatbotResponseEntity r LEFT JOIN FETCH r.possibleQuestions")
    List<ChatbotResponseEntity> findAllWithQuestions();

}
