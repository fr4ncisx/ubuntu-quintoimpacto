package com.ubuntu.ubuntu_app.controller;

import com.ubuntu.ubuntu_app.infra.statuses.BotChatDoc;
import com.ubuntu.ubuntu_app.service.ChatBotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chatbot")
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotService botService;

    @Operation(summary = "Hacer una pregunta al bot")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(defaultValue = BotChatDoc.bot_chatter)))
    @GetMapping("/faq")
    public ResponseEntity<?> chatbotResponse(@RequestParam String userQuestion) {
        return botService.getResponse(userQuestion);
    }

}
