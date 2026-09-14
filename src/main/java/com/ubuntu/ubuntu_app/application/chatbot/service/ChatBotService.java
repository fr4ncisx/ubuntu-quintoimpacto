package com.ubuntu.ubuntu_app.application.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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
import com.ubuntu.ubuntu_app.infrastructure.chatbot.adapter.mapper.ChatbotMapper;
import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;
import com.ubuntu.ubuntu_app.application.chatbot.api.ResponseCategories;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;
import com.ubuntu.ubuntu_app.shared.support.StopWords;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotService implements ChatBotUseCase {

    private final CosineSimilarity cosineSimilarity;
    private final ChatbotRepositoryPort chatbotRepository;
    private final ChatbotQuestionsRepositoryPort chatbotQuestionsRepository;
    private final ChatbotMapper chatbotMapper;
    private final ChatbotProperties chatbotProperties;
    private final List<ScoredAnswer> scoredAnswers = new ArrayList<>();

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
        String preprocessedQuestion = preprocessText(question);
        List<ChatbotResponseEntity> faqs;
        try {
            faqs = chatbotRepository.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while processing your request.", e);
        }
        scoredAnswers.clear();
        var faqsFiltered = faqs.stream()
                .filter(f -> f.getPossibleQuestions().stream().allMatch(q -> q.getCategory() == null))
                .collect(Collectors.toList());
        for (ChatbotResponseEntity faq : faqsFiltered) {
            for (ChatbotQuestionEntity questionEntity : faq.getPossibleQuestions()) {
                String preprocessedPossibleQuestion = preprocessText(questionEntity.getQuestion());
                double cosineScore = calculateCosineSimilarity(preprocessedQuestion, preprocessedPossibleQuestion);
                scoredAnswers.add(new ScoredAnswer(question, faq.getAnswer(), cosineScore));
            }
        }
        Optional<ScoredAnswer> bestMatch = scoredAnswers.stream()
                .filter(result -> result.similarityScore() >= chatbotProperties.similarity().threshold())
                .max(Comparator.comparing(ScoredAnswer::similarityScore));
        if (bestMatch.isPresent()) {
            var answerObtained = bestMatch.get().response();
            var similarityScore = bestMatch.get().similarityScore();
            return ResponseMap.responseGeneric("Respuesta", new BotAnswer(answerObtained, similarityScore));
        } else {
            return ResponseMap.botResponse("Lo siento, no pude comprender tu pregunta.");
        }
    }

    /**
     * Calculates the cosine similarity between two questions.
     *
     * @param question1 The first question (user input)
     * @param question2 The second question (database)
     * @return The cosine similarity score
     */
    private double calculateCosineSimilarity(String question1, String question2) {
        Map<CharSequence, Integer> vector1 = toVector(question1);
        Map<CharSequence, Integer> vector2 = toVector(question2);
        double similarity = cosineSimilarity.cosineSimilarity(vector1, vector2);
        return similarity;
    }

    /**
     * Converts text to vector
     *
     * @param text
     * @return converted text as vector
     */
    private Map<CharSequence, Integer> toVector(String text) {
        Map<CharSequence, Integer> vector = new HashMap<>();
        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            vector.put(token, vector.getOrDefault(token, 0) + 1);
        }
        return vector;
    }

    /**
     * Preprocesses the input text by tokenizing, stemming, and removing stop words.
     *
     * @param question The input text to preprocess
     * @return The preprocessed text
     */
    @SuppressWarnings("resource")
    private String preprocessText(String question) {
        if (question == null || question.isEmpty()) {
            return "";
        }
        try (TokenStream tokenStream = new WhitespaceAnalyzer().tokenStream(null, new StringReader(question))) {
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            StringBuilder result = new StringBuilder();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString().toLowerCase();
                term = term.replaceAll("[¿?!*]", "");
                if (!isStopWord(term)) {
                    result.append(term).append(' ');
                }
            }
            tokenStream.end();
            return result.length() > 0 ? result.substring(0, result.length() - 1).toLowerCase() : "";
        } catch (IOException e) {
            return question.toLowerCase().trim();
        }
    }

    private boolean isStopWord(String term) {
        return StopWords.getLatinAmericanSpanishStopWords().contains(term.toLowerCase());
    }

    public record ScoredAnswer(String userQuestion, String response, double similarityScore) {
    }

    public record BotAnswer(String answer, Double score) {
    }
}