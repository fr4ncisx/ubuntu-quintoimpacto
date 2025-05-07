package com.ubuntu.ubuntu_app.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.service.ChatBotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public final class PreloadCache implements ApplicationRunner {
    private final ChatBotService chatBotService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Caching categories from chatbot");
        chatBotService.obtainQuestions("INSTITUCIONAL");
        chatBotService.obtainQuestions("MICROEMPRENDIMIENTOS");
        chatBotService.obtainQuestions("PREGUNTAS_FRECUENTES");

        log.info("Caching responses of chatbot");
        for (long i = 1; i <= 11; i++) {
            chatBotService.answerResponseByIdAndFilteredCategory(i);
        }
        log.info("Loaded cache successfully");
    }
}
