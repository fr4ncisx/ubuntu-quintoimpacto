package com.ubuntu.ubuntu_app.springdoc;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.method.HandlerMethod;

import com.ubuntu.ubuntu_app.infrastructure.auth.web.AuthApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.auth.web.AuthV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.category.web.CategoryApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.chatbot.web.ChatbotApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.contact.web.ContactApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.country.web.CountryApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.country.web.CountryV1Controller;
import com.ubuntu.ubuntu_app.infrastructure.media.web.MediaApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.web.MicrobusinessApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.publication.web.PublicationApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.user.web.UserApiDocs;
import com.ubuntu.ubuntu_app.infrastructure.user.web.UserV1Controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiBeansTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(
                    OpenApiConfiguration.class,
                    UserApiDocs.class,
                    CountryApiDocs.class,
                    MicrobusinessApiDocs.class,
                    MediaApiDocs.class,
                    PublicationApiDocs.class,
                    ContactApiDocs.class,
                    CategoryApiDocs.class,
                    ChatbotApiDocs.class,
                    AuthApiDocs.class);

    @Test
    void allDocsBeansAreCreated() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(OpenAPI.class);
            assertThat(context).hasBean("usersApi");
            assertThat(context).hasBean("geoApi");
            assertThat(context).hasBean("microbusinessApi");
            assertThat(context).hasBean("mediaApi");
            assertThat(context).hasBean("publicationsApi");
            assertThat(context).hasBean("contactApi");
            assertThat(context).hasBean("categoriesApi");
            assertThat(context).hasBean("chatbotApi");
            assertThat(context).hasBean("authApi");
            assertThat(context).getBeans(GroupedOpenApi.class).hasSize(9);
            assertThat(context).getBeans(GlobalOperationCustomizer.class).hasSize(9);
        });
    }

    @Test
    void userCustomizerDocumentsOwnControllerOnly() throws Exception {
        HandlerMethod ownMethod = new HandlerMethod(new UserV1Controller(null),
                UserV1Controller.class.getMethod("getAllUsers", int.class, int.class, PagedResourcesAssembler.class));
        HandlerMethod foreignMethod = new HandlerMethod(new CountryV1Controller(null, null),
                CountryV1Controller.class.getMethod("findAllCountries"));

        runner.run(context -> {
            GlobalOperationCustomizer customizer = context.getBean("userOperations",
                    GlobalOperationCustomizer.class);

            Operation documented = customizer.customize(new Operation(), ownMethod);
            assertThat(documented.getSummary()).isEqualTo("List all users");
            assertThat(documented.getDescription()).isEqualTo("Returns paginated list of users");
            assertThat(documented.getTags()).isEqualTo(List.of("Users"));

            Operation untouched = customizer.customize(new Operation(), foreignMethod);
            assertThat(untouched.getSummary()).isNull();
            assertThat(untouched.getTags()).isNull();
        });
    }

    @Test
    void globalOpenApiHasInfoAndSecurity() {
        runner.run(context -> {
            OpenAPI openAPI = context.getBean(OpenAPI.class);
            assertThat(openAPI.getInfo().getTitle()).isEqualTo("Ubuntu API");
            assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("bearer-key");
            assertThat(openAPI.getTags()).hasSize(9);
        });
    }
}
