package com.ubuntu.ubuntu_app.infrastructure.chatbot.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.chatbot.port.in.ChatBotUseCase;
import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatBotV1Controller {

    private final ChatBotUseCase chatbotService;

    @GetMapping("/answers")
    public ResponseEntity<ApiResponse<Map<String, ?>>> askQuestion(@RequestParam String question) {
        return ResponseEntity.ok(ApiResponse.ok(chatbotService.answer(question)));
    }

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<ChatbotQuestionResponse>>> questionsByCategory(
            @RequestParam String category) {
        return ResponseEntity.ok(ApiResponse.ok(chatbotService.findQuestionsByCategory(category)));
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<ApiResponse<Object>> answerByQuestionId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(chatbotService.findAnswerByQuestionId(id)));
    }
}
