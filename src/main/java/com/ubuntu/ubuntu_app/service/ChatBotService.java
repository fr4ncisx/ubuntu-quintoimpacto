package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.FAQRepository;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.entities.FAQEntity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatBotService {

    @Autowired
    private FAQRepository faqRepository;

    @Value("${bot.threshold}")
    private int threshold;

    public ResponseEntity<?> getResponse(String userQuestion) {
        List<FAQEntity> faqs = faqRepository.findAll();
        String normalizedInput = userQuestion.toLowerCase().trim();
        String closestMatch = "";
        int minDistance = Integer.MAX_VALUE;
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        for (FAQEntity faq : faqs) {
            String question = faq.getQuestion().toLowerCase().trim();
            int distance = levenshtein.apply(normalizedInput, question);
            if (distance < minDistance) {
                minDistance = distance;
                closestMatch = faq.getAnswer();
            }
        }
        if (minDistance <= threshold) {
            var response = ResponseMap.botResponse(closestMatch);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseMap.botResponse("Lo siento, no entiendo tu pregunta"), HttpStatus.OK);
        }
    }
}
