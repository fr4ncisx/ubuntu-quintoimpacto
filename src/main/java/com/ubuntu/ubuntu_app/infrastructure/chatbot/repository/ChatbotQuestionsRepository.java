package com.ubuntu.ubuntu_app.infrastructure.chatbot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;


@Repository
public interface ChatbotQuestionsRepository extends JpaRepository<ChatbotQuestionEntity, Long>{
    
    @Query(value = "SELECT * FROM chatbot_questions WHERE category=:category", nativeQuery = true)
    List<ChatbotQuestionEntity> findQuestionsByCategory(String category);

    @Query(value = "SELECT * FROM chatbot_questions WHERE id=:id AND category IS NOT NULL", nativeQuery = true)
    Optional<ChatbotQuestionEntity> findByIdAndCategory(Long id);

    @Query(value = "SELECT answer_id FROM chatbot_questions WHERE id=:id", nativeQuery = true)
    Optional<Long> findFKofAnswers(Long id);
}
