package com.ubuntu.ubuntu_app.shared.chatbot.normalizer;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class UnicodeTextNormalizer implements TextNormalizer {

    private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{M}+");
    private static final Pattern NON_ALPHANUMERIC_PATTERN = Pattern.compile("[^a-z0-9\\s]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    private final Set<String> stopWords;

    public UnicodeTextNormalizer(Set<String> stopWords) {
        this.stopWords = stopWords == null ? Set.of() : Set.copyOf(stopWords);
    }

    @Override
    public List<String> normalize(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String decomposed = Normalizer.normalize(text, Normalizer.Form.NFD);
        String stripped = DIACRITICS_PATTERN.matcher(decomposed).replaceAll("");
        String cleaned = NON_ALPHANUMERIC_PATTERN.matcher(stripped.toLowerCase()).replaceAll(" ").trim();

        if (cleaned.isEmpty()) {
            return List.of();
        }

        String[] tokens = WHITESPACE_PATTERN.split(cleaned);
        List<String> result = new ArrayList<>(tokens.length);
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty() && !stopWords.contains(trimmed)) {
                result.add(trimmed);
            }
        }
        if (result.isEmpty()) {
            for (String token : tokens) {
                String trimmed = token.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
        }
        return result;
    }
}
