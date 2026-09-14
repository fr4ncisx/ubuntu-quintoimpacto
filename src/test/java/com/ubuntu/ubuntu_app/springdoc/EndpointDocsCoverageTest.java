package com.ubuntu.ubuntu_app.springdoc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.ubuntu.ubuntu_app.infrastructure.auth.web.AuthApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.auth.web.AuthV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.category.web.CategoryApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.category.web.CategoryV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.web.ChatBotV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.web.ChatbotApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.contact.web.ContactApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.contact.web.ContactRequestV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.country.web.CountryApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.country.web.CountryV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.media.web.CloudinaryV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.media.web.MediaApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.web.MicrobusinessApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.web.MicrobusinessV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.publication.web.PublicationApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.publication.web.PublicationV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.user.web.UserApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.user.web.UserV1Controller;

import static org.assertj.core.api.Assertions.assertThat;

class EndpointDocsCoverageTest {

    private static Set<String> handlerMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    @Test
    void everyUserEndpointIsDocumented() {
        assertThat(new UserApiDocs().documentedMethods()).isEqualTo(handlerMethods(UserV1Controller.class));
    }

    @Test
    void everyCountryEndpointIsDocumented() {
        assertThat(new CountryApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(CountryV1Controller.class));
    }

    @Test
    void everyMicrobusinessEndpointIsDocumented() {
        assertThat(new MicrobusinessApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(MicrobusinessV1Controller.class));
    }

    @Test
    void everyMediaEndpointIsDocumented() {
        assertThat(new MediaApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(CloudinaryV1Controller.class));
    }

    @Test
    void everyPublicationEndpointIsDocumented() {
        assertThat(new PublicationApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(PublicationV1Controller.class));
    }

    @Test
    void everyContactEndpointIsDocumented() {
        assertThat(new ContactApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(ContactRequestV1Controller.class));
    }

    @Test
    void everyCategoryEndpointIsDocumented() {
        assertThat(new CategoryApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(CategoryV1Controller.class));
    }

    @Test
    void everyChatbotEndpointIsDocumented() {
        assertThat(new ChatbotApiDocs().documentedMethods())
                .isEqualTo(handlerMethods(ChatBotV1Controller.class));
    }

    @Test
    void everyAuthEndpointIsDocumented() {
        assertThat(new AuthApiDocs().documentedMethods()).isEqualTo(handlerMethods(AuthV1Controller.class));
    }
}
