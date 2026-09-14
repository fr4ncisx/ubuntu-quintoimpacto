package com.ubuntu.ubuntu_app.infrastructure.chatbot.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubuntu.ubuntu_app.springdoc.EndpointDoc;

@Configuration
public class ChatbotApiDocs {

    private static final Map<String, EndpointDoc> DOCS = Map.of(
            "askQuestion",
            new EndpointDoc("Ask the chatbot", "Free-text question answering", "Chatbot"),
            "questionsByCategory",
            new EndpointDoc("Questions by category", "Returns FAQ questions of a category", "Chatbot"),
            "answerByQuestionId",
            new EndpointDoc("Answer by question id", "Returns the answer for a question", "Chatbot"));

    @Bean
    public GroupedOpenApi chatbotApi() {
        return GroupedOpenApi.builder().group("chatbot").pathsToMatch("/api/v1/chatbot/**").build();
    }

    @Bean
    public GlobalOperationCustomizer chatbotOperations() {
        return (operation, handlerMethod) -> {
            if (!handlerMethod.getBeanType().equals(ChatBotV1Controller.class)) {
                return operation;
            }
            EndpointDoc doc = DOCS.get(handlerMethod.getMethod().getName());
            if (doc != null) {
                operation.setSummary(doc.summary());
                operation.setDescription(doc.description());
                operation.setTags(List.of(doc.tag()));
            }
            return operation;
        };
    }

    public Set<String> documentedMethods() {
        return DOCS.keySet();
    }
}
