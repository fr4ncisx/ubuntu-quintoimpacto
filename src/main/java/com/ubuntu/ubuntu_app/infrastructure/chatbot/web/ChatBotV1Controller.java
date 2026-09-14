package com.ubuntu.ubuntu_app.infrastructure.chatbot.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.chatbot.port.in.ChatBotUseCase;
import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@Validated
public class ChatBotV1Controller {

    private final ChatBotUseCase chatbotService;

    @GetMapping("/answers")
    public ResponseEntity<ApiResponse<Map<String, ?>>> askQuestion(
            @RequestParam @Size(max = 500, message = "La pregunta no debe exceder 500 caracteres") String question) {
        return ResponseEntity.ok(ApiResponse.ok(chatbotService.answer(question)));
    }

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<ChatbotQuestionResponse>>> questionsByCategory(
            @RequestParam @NotBlank(message = "La categoria no debe estar vacia") String category) {
        return ResponseEntity.ok(ApiResponse.ok(chatbotService.findQuestionsByCategory(category)));
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<ApiResponse<Object>> answerByQuestionId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(chatbotService.findAnswerByQuestionId(id)));
    }
}
