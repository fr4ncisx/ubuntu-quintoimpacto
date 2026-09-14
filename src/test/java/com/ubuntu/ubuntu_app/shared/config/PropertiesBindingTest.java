package com.ubuntu.ubuntu_app.shared.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesBindingTest {

    @Configuration
    @EnableConfigurationProperties({
            SecurityProperties.class,
            TokenProperties.class,
            CorsProperties.class,
            EmailProperties.class,
            NominatimProperties.class,
            ChatbotProperties.class,
            JwtProperties.class,
            CloudinaryProperties.class
    })
    static class TestConfig {
    }

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TestConfig.class))
            .withPropertyValues(
                    "app.security.google.post-login-redirect=http://front",
                    "app.security.jwt.cookie-name=ubuntu_jwt",
                    "app.security.jwt.refresh-cookie-name=ubuntu_refresh",
                    "token.expiration=15",
                    "token.refresh-expiration=10080",
                    "cors.vercel=http://a",
                    "cors.koyeb=http://b",
                    "cors.local=http://c",
                    "email.address=a@b.com",
                    "email.password=secret",
                    "nominatim.search=http://s",
                    "nominatim.reverse=http://r",
                    "chatbot.similarity.threshold=0.5",
                    "jwt.secret.key=0123456789abcdef0123456789abcdef",
                    "cloudinary.url-config=cloudinary://k:s@c");

    @Test
    void bindsAllProperties() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(SecurityProperties.class);
            SecurityProperties security = context.getBean(SecurityProperties.class);
            assertThat(security.google().postLoginRedirect()).isEqualTo("http://front");
            assertThat(security.jwt().cookieName()).isEqualTo("ubuntu_jwt");
            assertThat(context.getBean(TokenProperties.class).expiration()).isEqualTo(15);
            assertThat(context.getBean(CorsProperties.class).local()).isEqualTo("http://c");
            assertThat(context.getBean(ChatbotProperties.class).similarity().threshold())
                    .isEqualTo(0.5);
        });
    }

    @Test
    void blankRequiredValueFailsFast() {
        runner.withPropertyValues("app.security.jwt.cookie-name=")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void zeroExpirationFailsFast() {
        runner.withPropertyValues("token.expiration=0")
                .run(context -> assertThat(context).hasFailed());
    }
}
