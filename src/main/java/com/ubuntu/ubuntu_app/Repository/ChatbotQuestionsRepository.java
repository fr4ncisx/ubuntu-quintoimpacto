package com.ubuntu.ubuntu_app.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.ChatbotQuestionEntity;


@Repository
public interface ChatbotQuestionsRepository extends JpaRepository<ChatbotQuestionEntity, Long>{
    
    @Query(value = "SELECT * FROM chatbot_questions WHERE categoria=:category", nativeQuery = true)
    List<ChatbotQuestionEntity> obtainQuestions(String category);

    @Query(value = "SELECT * FROM chatbot_questions WHERE id=:id AND categoria IS NOT NULL", nativeQuery = true)
    Optional<ChatbotQuestionEntity> findByIdAndCategory(Long id);

    @Query(value = "SELECT id_answer FROM chatbot_questions WHERE id=:id", nativeQuery = true)
    Optional<Long> findFKofAnswers(Long id);
}
