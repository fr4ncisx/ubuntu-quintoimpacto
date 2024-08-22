package com.ubuntu.ubuntu_app.controller;

import com.ubuntu.ubuntu_app.service.ChatBotService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chatbot")
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

    private final ChatBotService chatbotService;

    @GetMapping("/init")
    public ResponseEntity<?> chatbotResponse(@RequestParam String question) {
        return chatbotService.getResponse(question);
    }

    @GetMapping("/category")
    public ResponseEntity<?> chatbotCategory(@RequestParam String name) {
        return chatbotService.obtainQuestions(name);
    }

    @GetMapping("/response")
    public ResponseEntity<?> responseDefaultQuestions(@RequestParam Long id) {
        return chatbotService.answerResponseByIdAndFilteredCategory(id);
    }
}
