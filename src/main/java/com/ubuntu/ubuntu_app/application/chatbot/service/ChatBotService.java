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
    private volatile List<QuestionMatcher.Candidate> cachedCandidates;

    @Override
    @Transactional(readOnly = true)
    public List<ChatbotQuestionResponse> findQuestionsByCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalParameterException("Please write a category to search questions");
        }
        var questionsFound = chatbotQuestionsRepository.findQuestionsByCategory(category.trim().toUpperCase());
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
        var answerId = chatbotQuestionsRepository.findFKofAnswers(id)
                .orElseThrow(() -> new SqlEmptyResponse("There are no answers with that foreign key"));
        var foundAnswer = chatbotRepository.findById(answerId);
        if (foundAnswer.isEmpty()) {
            throw new SqlEmptyResponse("There are no answers with that foreign key");
        }
        if (foundAnswer.get().getAnswer().equalsIgnoreCase("Respuesta categorias")) {
            return ResponseMap.multiBotAnswer(ResponseCategories.response);
        }
        return ResponseMap.botResponse(foundAnswer.get().getAnswer());
    }

    @Override
    @Cacheable(value = "botResponses", key = "#question?.trim()?.toLowerCase() ?? 'empty'")
    @Transactional(readOnly = true)
    public Map<String, ?> answer(String question) {
        if (question != null && question.length() > 500) {
            throw new IllegalParameterException("Question must not exceed 500 characters");
        }
        var normalized = question == null ? "" : question.trim().toLowerCase();
        var bestMatch = questionMatcher.bestMatch(normalized, candidates(),
                chatbotProperties.similarity().threshold());
        if (bestMatch.isPresent()) {
            var match = bestMatch.get();
            return ResponseMap.responseGeneric("Respuesta", new BotAnswer(match.answer(), match.score()));
        }
        return ResponseMap.botResponse("Lo siento, no pude comprender tu pregunta.");
    }

    void invalidateCandidates() {
        cachedCandidates = null;
    }

    private List<QuestionMatcher.Candidate> candidates() {
        var result = cachedCandidates;
        if (result == null) {
            synchronized (this) {
                result = cachedCandidates;
                if (result == null) {
                    result = loadCandidates();
                    cachedCandidates = result;
                }
            }
        }
        return result;
    }

    private List<QuestionMatcher.Candidate> loadCandidates() {
        List<ChatbotResponseEntity> faqs;
        try {
            faqs = chatbotRepository.findAllWithQuestions();
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while processing your request.", e);
        }
        return faqs.stream()
                .filter(f -> f.getPossibleQuestions().stream().allMatch(q -> q.getCategory() == null))
                .flatMap(f -> f.getPossibleQuestions().stream()
                        .map(q -> new QuestionMatcher.Candidate(q.getQuestion(), f.getAnswer())))
                .toList();
    }

    public record BotAnswer(String answer, Double score) {
    }
}