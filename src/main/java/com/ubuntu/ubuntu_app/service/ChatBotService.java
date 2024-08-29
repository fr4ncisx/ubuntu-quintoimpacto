package com.ubuntu.ubuntu_app.service;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubuntu.ubuntu_app.Repository.ChatbotQuestionsRepository;
import com.ubuntu.ubuntu_app.Repository.ChatbotRepository;
import com.ubuntu.ubuntu_app.infra.errors.IllegalParameterException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.ChatbotCategoriesDTO;
import com.ubuntu.ubuntu_app.model.dto.ResponseCategories;
import com.ubuntu.ubuntu_app.model.entities.ChatbotQuestionEntity;
import com.ubuntu.ubuntu_app.model.entities.ChatbotResponseEntity;
import com.ubuntu.ubuntu_app.model.filters.StopWords;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Lazy
@Service
public class ChatBotService {

    private final CosineSimilarity cosineSimilarity;
    private final ChatbotRepository chatbotRepository;
    private final ChatbotQuestionsRepository chatbotQuestionsRepository;
    private final ModelMapper modelMapper;
    private final List<InnerChatBotService> debugChatBot = new ArrayList<>();

    @Value("${chatbot.similarity.threshold:0.5}")
    private double similarityThreshold;

    public ChatBotService(ChatbotRepository chatbotRepository, CosineSimilarity cosineSimilarity,
            ChatbotQuestionsRepository chatbotQuestionsRepository, ModelMapper modelMapper) {
        this.chatbotRepository = chatbotRepository;
        this.cosineSimilarity = cosineSimilarity;
        this.chatbotQuestionsRepository = chatbotQuestionsRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> obtainQuestions(String category) {
        if (category.isBlank()) {
            throw new IllegalParameterException("Please write a category to search questions");
        }
        var questionsFound = chatbotQuestionsRepository.obtainQuestions(category);
        if (questionsFound.isEmpty()) {
            throw new SqlEmptyResponse("No questions found");
        }
        var questionsDTO = questionsFound.stream()
                .map(q -> modelMapper.map(q, ChatbotCategoriesDTO.class)).toList();
        return ResponseEntity.ok(questionsDTO);
    }

    @Cacheable(value = "botResponseDefault", key = "#id")
    public ResponseEntity<?> answerResponseByIdAndFilteredCategory(Long id) {
        var foundQuestion = chatbotQuestionsRepository.findByIdAndCategory(id);
        if (!foundQuestion.isPresent()) {
            throw new SqlEmptyResponse("There are no questions with that id");
        }
        var getFKofAnswer = chatbotQuestionsRepository.findFKofAnswers(id);
        var foundAnswer = chatbotRepository.findById(getFKofAnswer.get());
        if (!foundAnswer.isPresent()) {
            throw new SqlEmptyResponse("There are no answers with that foreign key");
        }
        if (foundAnswer.get().getAnswer().equalsIgnoreCase("Respuesta categorias")) {
            return ResponseEntity.ok(ResponseMap.MultiBotResponse(ResponseCategories.response));
        }
        return ResponseEntity.ok(ResponseMap.botResponse(foundAnswer.get().getAnswer()));
    }

    /**
     * Processes a user question and returns the best matching response.
     * 
     * @param question The user's question
     * @return ResponseEntity containing the bot's response
     * @throws InterruptedException
     */
    @Cacheable(value = "botResponses", key = "#question")
    public ResponseEntity<?> getResponse(String question) {
        String preprocessedQuestion = preprocessText(question);
        List<ChatbotResponseEntity> faqs;
        try {
            faqs = chatbotRepository.findAll();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseMap.botResponse("An error occurred while processing your request."));
        }
        debugChatBot.clear();
        var faqsFiltered = faqs.stream()
                .filter(f -> f.getPossibleQuestions().stream().allMatch(q -> q.getCategory() == null))
                .collect(Collectors.toList());
        for (ChatbotResponseEntity faq : faqsFiltered) {
            for (ChatbotQuestionEntity questionEntity : faq.getPossibleQuestions()) {
                String preprocessedPossibleQuestion = preprocessText(questionEntity.getQuestion());
                double cosineScore = calculateCosineSimilarity(preprocessedQuestion, preprocessedPossibleQuestion);
                debugChatBot.add(new InnerChatBotService(question, faq.getAnswer(), cosineScore));
            }
        }
        Optional<InnerChatBotService> bestMatch = debugChatBot.stream()
                .filter(result -> result.similarityScore() >= similarityThreshold)
                .max(Comparator.comparing(InnerChatBotService::similarityScore));
        if (bestMatch.isPresent()) {
            var answerObtained = bestMatch.get().response();
            var similarityScore = bestMatch.get().similarityScore();
            return ResponseEntity
                    .ok(ResponseMap.responseGeneric("Respuesta", new BotResponse(answerObtained, similarityScore)));
        } else {
            return ResponseEntity.ok(ResponseMap.botResponse("Lo siento, no pude comprender tu pregunta."));
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

    public record InnerChatBotService(String userQuestion, String response, double similarityScore) {
    }

    public record BotResponse(String answerFound, Double similarityScore) {
    }
}