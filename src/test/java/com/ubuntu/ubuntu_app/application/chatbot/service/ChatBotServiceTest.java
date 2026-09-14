package com.ubuntu.ubuntu_app.application.chatbot.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ubuntu.ubuntu_app.application.chatbot.ChatbotCategory;
import com.ubuntu.ubuntu_app.application.chatbot.api.ChatbotQuestionResponse;
import com.ubuntu.ubuntu_app.application.chatbot.api.ResponseCategories;
import com.ubuntu.ubuntu_app.application.chatbot.port.out.ChatbotQuestionsRepositoryPort;
import com.ubuntu.ubuntu_app.application.chatbot.port.out.ChatbotRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.adapter.mapper.ChatbotMapper;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotQuestionEntity;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.entity.ChatbotResponseEntity;
import com.ubuntu.ubuntu_app.shared.config.ChatbotProperties;
import com.ubuntu.ubuntu_app.shared.error.IllegalParameterException;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;

class ChatBotServiceTest {

    private ChatBotService service(ChatbotRepositoryPort repo, ChatbotQuestionsRepositoryPort questions,
            ChatbotMapper mapper) {
        return new ChatBotService(repo, questions, mapper,
                new ChatbotProperties(new ChatbotProperties.Similarity(0.5)));
    }

    private ChatbotResponseEntity faq(String answer, String... questions) {
        List<ChatbotQuestionEntity> list = java.util.Arrays.stream(questions)
                .map(q -> new ChatbotQuestionEntity(null, q, null))
                .toList();
        return new ChatbotResponseEntity(null, answer, list);
    }

    @Test
    void answerReturnsBestMatch() {
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("usa el boton contactar",
                "como invertir microemprendimiento", "quiero invertir")));
        var service = service(repo, Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        var result = service.answer("como invertir en un microemprendimiento");

        assertTrue(result.containsKey("Respuesta"));
        var botAnswer = (ChatBotService.BotAnswer) result.get("Respuesta");
        assertEquals("usa el boton contactar", botAnswer.answer());
        Mockito.verify(repo, Mockito.never()).findAll();
    }

    @Test
    void answerFallsBackWhenNothingMatches() {
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("usa el boton contactar",
                "como invertir microemprendimiento")));
        var service = service(repo, Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        var result = service.answer("receta de empanadas criollas");

        assertEquals("Lo siento, no pude comprender tu pregunta.", result.get("Respuesta"));
    }

    @Test
    void answerBuildsCandidatesOnlyOnce() {
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("usa el boton contactar",
                "como invertir microemprendimiento")));
        var service = service(repo, Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        service.answer("como invertir");
        service.answer("receta de empanadas criollas");
        service.answer("como invertir microemprendimiento");

        Mockito.verify(repo, Mockito.times(1)).findAllWithQuestions();
    }

    @Test
    void answerBuildsCandidatesOnceUnderConcurrency() throws InterruptedException {
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("usa el boton contactar",
                "como invertir microemprendimiento")));
        var service = service(repo, Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        int threads = 32;
        var executor = java.util.concurrent.Executors.newFixedThreadPool(threads);
        var latch = new java.util.concurrent.CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    service.answer("como invertir microemprendimiento");
                } finally {
                    latch.countDown();
                }
            });
        }
        assertTrue(latch.await(10, java.util.concurrent.TimeUnit.SECONDS));
        executor.shutdown();

        Mockito.verify(repo, Mockito.times(1)).findAllWithQuestions();
    }

    @Test
    void answerNormalizesCasing() {
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("usa el boton contactar",
                "como invertir microemprendimiento")));
        var service = service(repo, Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        var upper = service.answer("COMO INVERTIR");
        var mixed = service.answer("  Como Invertir  ");

        assertEquals(service.answer("como invertir"), upper);
        assertEquals(service.answer("como invertir"), mixed);
    }

    @Test
    void answerReloadsCandidatesAfterInvalidation() {
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("respuesta original",
                "como invertir microemprendimiento")));
        var service = service(repo, Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        var first = (ChatBotService.BotAnswer) service.answer("como invertir").get("Respuesta");
        assertEquals("respuesta original", first.answer());

        Mockito.when(repo.findAllWithQuestions()).thenReturn(List.of(faq("respuesta actualizada",
                "como invertir microemprendimiento")));
        service.invalidateCandidates();

        var second = (ChatBotService.BotAnswer) service.answer("como invertir").get("Respuesta");
        assertEquals("respuesta actualizada", second.answer());
        Mockito.verify(repo, Mockito.times(2)).findAllWithQuestions();
    }

    @Test
    void answerRejectsOverlongQuestion() {
        var service = service(Mockito.mock(ChatbotRepositoryPort.class),
                Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        assertThrows(IllegalParameterException.class, () -> service.answer("x".repeat(501)));
    }

    @Test
    void findQuestionsByCategoryIsCaseInsensitive() {
        ChatbotQuestionsRepositoryPort questions = Mockito.mock(ChatbotQuestionsRepositoryPort.class);
        var entity = new ChatbotQuestionEntity(1L, "Quienes somos?", ChatbotCategory.INSTITUTIONAL);
        Mockito.when(questions.findQuestionsByCategory("INSTITUTIONAL")).thenReturn(List.of(entity));
        ChatbotMapper mapper = Mockito.mock(ChatbotMapper.class);
        Mockito.when(mapper.toQuestionResponse(entity)).thenReturn(new ChatbotQuestionResponse(1L, "Quienes somos?"));
        var service = service(Mockito.mock(ChatbotRepositoryPort.class), questions, mapper);

        var result = service.findQuestionsByCategory("institutional");

        assertEquals(1, result.size());
        assertEquals("Quienes somos?", result.get(0).question());
        Mockito.verify(questions).findQuestionsByCategory("INSTITUTIONAL");
    }

    @Test
    void findQuestionsByCategoryRejectsBlankAndNull() {
        var service = service(Mockito.mock(ChatbotRepositoryPort.class),
                Mockito.mock(ChatbotQuestionsRepositoryPort.class),
                Mockito.mock(ChatbotMapper.class));

        assertThrows(IllegalParameterException.class, () -> service.findQuestionsByCategory("  "));
        assertThrows(IllegalParameterException.class, () -> service.findQuestionsByCategory(null));
    }

    @Test
    void findQuestionsByCategoryEmptyIsNotFound() {
        ChatbotQuestionsRepositoryPort questions = Mockito.mock(ChatbotQuestionsRepositoryPort.class);
        Mockito.when(questions.findQuestionsByCategory("FOO")).thenReturn(List.of());
        var service = service(Mockito.mock(ChatbotRepositoryPort.class), questions,
                Mockito.mock(ChatbotMapper.class));

        assertThrows(SqlEmptyResponse.class, () -> service.findQuestionsByCategory("FOO"));
    }

    @Test
    void findAnswerByQuestionIdReturnsAnswer() {
        ChatbotQuestionsRepositoryPort questions = Mockito.mock(ChatbotQuestionsRepositoryPort.class);
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        var question = new ChatbotQuestionEntity(2L, "Objetivos?", ChatbotCategory.INSTITUTIONAL);
        Mockito.when(questions.findByIdAndCategory(2L)).thenReturn(Optional.of(question));
        Mockito.when(questions.findFKofAnswers(2L)).thenReturn(Optional.of(7L));
        Mockito.when(repo.findById(7L))
                .thenReturn(Optional.of(new ChatbotResponseEntity(7L, "Desarrollar instrumentos.", List.of())));
        var service = service(repo, questions, Mockito.mock(ChatbotMapper.class));

        var result = service.findAnswerByQuestionId(2L);

        assertTrue(result instanceof java.util.Map);
        assertEquals("Desarrollar instrumentos.", ((java.util.Map<?, ?>) result).get("Respuesta"));
    }

    @Test
    void findAnswerByQuestionIdMissingQuestionIsNotFound() {
        ChatbotQuestionsRepositoryPort questions = Mockito.mock(ChatbotQuestionsRepositoryPort.class);
        Mockito.when(questions.findByIdAndCategory(99L)).thenReturn(Optional.empty());
        var service = service(Mockito.mock(ChatbotRepositoryPort.class), questions,
                Mockito.mock(ChatbotMapper.class));

        assertThrows(SqlEmptyResponse.class, () -> service.findAnswerByQuestionId(99L));
    }

    @Test
    void findAnswerByQuestionIdMissingFkIsNotFound() {
        ChatbotQuestionsRepositoryPort questions = Mockito.mock(ChatbotQuestionsRepositoryPort.class);
        var question = new ChatbotQuestionEntity(2L, "Objetivos?", ChatbotCategory.INSTITUTIONAL);
        Mockito.when(questions.findByIdAndCategory(2L)).thenReturn(Optional.of(question));
        Mockito.when(questions.findFKofAnswers(2L)).thenReturn(Optional.empty());
        var service = service(Mockito.mock(ChatbotRepositoryPort.class), questions,
                Mockito.mock(ChatbotMapper.class));

        assertThrows(SqlEmptyResponse.class, () -> service.findAnswerByQuestionId(2L));
    }

    @Test
    void findAnswerByQuestionIdCategoriesTriggerReturnsMultiResponse() {
        ChatbotQuestionsRepositoryPort questions = Mockito.mock(ChatbotQuestionsRepositoryPort.class);
        ChatbotRepositoryPort repo = Mockito.mock(ChatbotRepositoryPort.class);
        var question = new ChatbotQuestionEntity(6L, "Categorias?", ChatbotCategory.MICROBUSINESS);
        Mockito.when(questions.findByIdAndCategory(6L)).thenReturn(Optional.of(question));
        Mockito.when(questions.findFKofAnswers(6L)).thenReturn(Optional.of(6L));
        Mockito.when(repo.findById(6L))
                .thenReturn(Optional.of(new ChatbotResponseEntity(6L, "Respuesta categorias", List.of())));
        var service = service(repo, questions, Mockito.mock(ChatbotMapper.class));

        var result = service.findAnswerByQuestionId(6L);

        assertTrue(result instanceof ResponseCategories.MultiResponseList);
        assertEquals(4, ((ResponseCategories.MultiResponseList) result).Respuestas().size());
    }
}
