package com.ubuntu.ubuntu_app.infrastructure.chatbot.web;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ubuntu.ubuntu_app.application.chatbot.port.in.ChatBotUseCase;
import com.ubuntu.ubuntu_app.shared.error.GlobalErrorHandler;
import com.ubuntu.ubuntu_app.shared.error.IllegalParameterException;

class ChatBotV1ControllerTest {

    private ChatBotUseCase useCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        useCase = Mockito.mock(ChatBotUseCase.class);
        Mockito.doReturn(Map.of("Respuesta", "ok")).when(useCase).answer(anyString());
        mockMvc = MockMvcBuilders.standaloneSetup(new ChatBotV1Controller(useCase))
                .setControllerAdvice(new GlobalErrorHandler())
                .build();
    }

    @Test
    void validQuestionIsAccepted() throws Exception {
        mockMvc.perform(get("/api/v1/chatbot/answers").param("question", "como invertir"))
                .andExpect(status().isOk());
    }

    @Test
    void serviceValidationFailureMapsToBadRequest() throws Exception {
        Mockito.doThrow(new IllegalParameterException("Question must not exceed 500 characters"))
                .when(useCase).answer(anyString());

        mockMvc.perform(get("/api/v1/chatbot/answers").param("question", "x".repeat(501)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void apiResponseEnvelopeIsPreserved() throws Exception {
        var body = mockMvc.perform(get("/api/v1/chatbot/answers").param("question", "como invertir"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(body.contains("\"message\":\"OK\""));
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void constraintViolationMapsToBadRequest() {
        var violation = (jakarta.validation.ConstraintViolation<?>)(jakarta.validation.ConstraintViolation) Mockito
                .mock(jakarta.validation.ConstraintViolation.class);
        Mockito.when(violation.getPropertyPath()).thenReturn(Mockito.mock(jakarta.validation.Path.class));
        Mockito.when(violation.getMessage()).thenReturn("La pregunta no debe exceder 500 caracteres");
        var ex = new jakarta.validation.ConstraintViolationException(Set.of(violation));

        var response = new GlobalErrorHandler().constraintViolations(ex);

        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().size() == 1);
    }
}
