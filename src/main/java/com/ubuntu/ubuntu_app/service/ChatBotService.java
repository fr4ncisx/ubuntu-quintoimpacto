package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.FAQRepository;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.AnswerThreshold;
import com.ubuntu.ubuntu_app.model.entities.FAQEntity;

import lombok.RequiredArgsConstructor;

import org.apache.commons.text.similarity.JaccardSimilarity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatBotService {

    private final FAQRepository faqRepository;

    @Value("${bot.threshold}")
    private Double threshold; // 0.6 min score valid

    public ResponseEntity<?> getResponse(String userQuestion) {
        List<FAQEntity> faqs = faqRepository.findAll();
        List<AnswerThreshold> listOfValidAnswers = new ArrayList<>();
        String normalizedInput = userQuestion.toLowerCase().trim();
        JaccardSimilarity similarity = new JaccardSimilarity();
        Double distance;
        String closestMatch = null;
        for (FAQEntity faq : faqs) {
            String question = faq.getQuestion().toLowerCase().trim();
            distance = similarity.apply(normalizedInput, question);
            listOfValidAnswers.add(new AnswerThreshold(faq.getQuestion(),
                    faq.getAnswer(), distance));
        }
        for (AnswerThreshold answer : listOfValidAnswers) {
            if (answer.getDistance() >= threshold) {
                closestMatch = answer.getAnswer();
                break;
            } else if (answer.getDistance() < threshold) {
                closestMatch = "Lo siento no pude entender tu pregunta";
            }
        }
        return new ResponseEntity<>(ResponseMap.botResponse(closestMatch), HttpStatus.OK);
    }
}
