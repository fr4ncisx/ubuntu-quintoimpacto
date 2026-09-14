package com.ubuntu.ubuntu_app.shared.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequiredEnvValidator implements CommandLineRunner {

    private final Environment env;

    public RequiredEnvValidator(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) {
        List<String> problems = new ArrayList<>();
        requireNonBlank(problems, "POSTGRES_USER");
        requireNonBlank(problems, "POSTGRES_PASSWORD");
        requireNonBlank(problems, "POSTGRES_URL");
        requireNonBlank(problems, "SECRET_KEY");
        requireNonBlank(problems, "CLOUDINARY");
        requireNonBlank(problems, "GOOGLE_CLIENT_ID");
        requireNonBlank(problems, "GOOGLE_CLIENT_SECRET");
        requireNonBlank(problems, "FRONTEND_IP");
        requireNonBlank(problems, "PROD_IP");
        rejectPlaceholders(problems, "FRONTEND_IP", List.of("http://ip:port"));
        rejectPlaceholders(problems, "PROD_IP", List.of("KOYEB URL"));
        rejectPlaceholders(problems, "GOOGLE_CLIENT_ID", List.of("Fill with Google client id"));
        rejectPlaceholders(problems, "GOOGLE_CLIENT_SECRET", List.of("Fill with Google client secret"));
        rejectPlaceholders(problems, "GOOGLE_CLIENT_SECRET", List.of("Fill with Google client secret"));
        rejectPlaceholders(problems, "CLOUDINARY",
                List.of("cloudinary://PUBLIC API KEY:SECRET API KEY@CLOUD NAME"));
        requireUrl(problems, "FRONTEND_IP");
        requireUrl(problems, "PROD_IP");
        requireUrl(problems, "FRONTEND_POST_LOGIN_URL");
        requireMinLength(problems, "SECRET_KEY", 32);
        requireMatches(problems, "CLOUDINARY", "^cloudinary://[^:]+:[^@]+@.+",
                "debe tener formato cloudinary://API_KEY:API_SECRET@CLOUD_NAME");
        requirePositiveInt(problems, "token.expiration");
        if (isMailEnabled()) {
            requireNonBlank(problems, "EMAIL_SENDER");
            requireNonBlank(problems, "EMAIL_SENDER_PASSWORD");
            rejectPlaceholders(problems, "EMAIL_SENDER", List.of("Fill with sender email address"));
            rejectPlaceholders(problems, "EMAIL_SENDER_PASSWORD",
                    List.of("Fill with sender app password"));
        }
        if (!problems.isEmpty()) {
            throw new IllegalStateException(
                    "Configuración inválida, corrige estas variables antes de arrancar:%n - %s"
                            .formatted(String.join("%n - ".formatted(), problems)));
        }
    }

    private boolean isMailEnabled() {
        return Boolean.parseBoolean(env.getProperty("app.mail.enabled", "false"));
    }

    private String value(String key) {
        String value = env.getProperty(key);
        return value == null ? null : value.trim();
    }

    private void requireNonBlank(List<String> problems, String key) {
        String value = value(key);
        if (value == null || value.isBlank()) {
            problems.add(key + " falta o está vacía");
        }
    }

    private void rejectPlaceholders(List<String> problems, String key, List<String> placeholders) {
        String value = value(key);
        if (value == null) {
            return;
        }
        for (String placeholder : placeholders) {
            if (value.equals(placeholder) || value.contains("Fill with")) {
                problems.add(key + " todavía tiene valor placeholder (" + value + ")");
                return;
            }
        }
    }

    private void requireUrl(List<String> problems, String key) {
        String value = value(key);
        if (value == null || value.isBlank()) {
            return;
        }
        try {
            URI uri = new URI(value);
            if (uri.getScheme() == null || uri.getHost() == null) {
                problems.add(key + " no es una URL válida (" + value + ")");
            }
        } catch (Exception e) {
            problems.add(key + " no es una URL válida (" + value + ")");
        }
    }

    private void requireMinLength(List<String> problems, String key, int min) {
        String value = value(key);
        if (value != null && value.length() < min) {
            problems.add(key + " debe tener al menos " + min + " caracteres");
        }
    }

    private void requireMatches(List<String> problems, String key, String regex, String hint) {
        String value = value(key);
        if (value != null && !value.isBlank() && !value.matches(regex)) {
            problems.add(key + " " + hint);
        }
    }

    private void requirePositiveInt(List<String> problems, String key) {
        String value = value(key);
        try {
            if (value == null || Integer.parseInt(value) <= 0) {
                problems.add(key + " debe ser un entero mayor a 0");
            }
        } catch (NumberFormatException e) {
            problems.add(key + " debe ser un entero mayor a 0");
        }
    }
}
