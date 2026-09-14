package com.ubuntu.ubuntu_app.application.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.shared.config.ChatbotProperties;

import com.ubuntu.ubuntu_app.application.chatbot.port.in.ChatBotUseCase;
import com.ubuntu.ubuntu_app.application.chatbot.port.out.ChatbotQuestionsRepositoryPort;
import com.ubuntu.ubuntu_app.application.chatbot.port.out.ChatbotRepositoryPort;
import com.ubuntu.ubuntu_app.shared.error.IllegalParameterException;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.shared.api.ResponseMap;
import com.ubuntu.ubuntu_app.shared.chatbot.QuestionMatcher;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.adapter.mapper.ChatbotMapper;
import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;
import com.ubuntu.ubuntu_app.application.chatbot.api.ResponseCategories;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;
import com.ubuntu.ubuntu_app.shared.support.StopWords;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotService implements ChatBotUseCase {

    private final ChatbotRepositoryPort chatbotRepository;
    private final ChatbotQuestionsRepositoryPort chatbotQuestionsRepository;
    private final ChatbotMapper chatbotMapper;
    private final ChatbotProperties chatbotProperties;
    private final QuestionMatcher questionMatcher = new QuestionMatcher(
            StopWords.getLatinAmericanSpanishStopWords());

    @Override
    @Transactional(readOnly = true)
    public List<ChatbotQuestionResponse> findQuestionsByCategory(String category) {
        if (category.isBlank()) {
            throw new IllegalParameterException("Please write a category to search questions");
        }
        var questionsFound = chatbotQuestionsRepository.findQuestionsByCategory(category);
        if (questionsFound.isEmpty()) {
            throw new SqlEmptyResponse("No questions found");
        }
        return questionsFound.stream()
                .map(chatbotMapper::toQuestionResponse).toList();
    }

    @Override
    @Cacheable(value = "botResponseDefault", key = "#id")
    @Transactional(readOnly = true)
    public Object findAnswerByQuestionId(Long id) {
        var foundQuestion = chatbotQuestionsRepository.findByIdAndCategory(id);
        if (foundQuestion.isEmpty()) {
            throw new SqlEmptyResponse("There are no questions with that id");
        }
        var getFKofAnswer = chatbotQuestionsRepository.findFKofAnswers(id);
        var foundAnswer = chatbotRepository.findById(getFKofAnswer.get());
        if (foundAnswer.isEmpty()) {
            throw new SqlEmptyResponse("There are no answers with that foreign key");
        }
        if (foundAnswer.get().getAnswer().equalsIgnoreCase("Respuesta categorias")) {
            return ResponseMap.multiBotAnswer(ResponseCategories.response);
        }
        return ResponseMap.botResponse(foundAnswer.get().getAnswer());
    }

    /**
     * Processes a user question and returns the best matching response.
     *
     * @param question The user's question
     * @return Map containing the bot's response
     */
    @Override
    @Cacheable(value = "botResponses", key = "#question")
    @Transactional(readOnly = true)
    public Map<String, ?> answer(String question) {
        List<ChatbotResponseEntity> faqs;
        try {
            faqs = chatbotRepository.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while processing your request.", e);
        }
        List<QuestionMatcher.Candidate> candidates = faqs.stream()
                .filter(f -> f.getPossibleQuestions().stream().allMatch(q -> q.getCategory() == null))
                .flatMap(f -> f.getPossibleQuestions().stream()
                        .map(q -> new QuestionMatcher.Candidate(q.getQuestion(), f.getAnswer())))
                .toList();
        var bestMatch = questionMatcher.bestMatch(question, candidates,
                chatbotProperties.similarity().threshold());
        if (bestMatch.isPresent()) {
            var match = bestMatch.get();
            return ResponseMap.responseGeneric("Respuesta", new BotAnswer(match.answer(), match.score()));
        }
        return ResponseMap.botResponse("Lo siento, no pude comprender tu pregunta.");
    }

    public record BotAnswer(String answer, Double score) {
    }
}