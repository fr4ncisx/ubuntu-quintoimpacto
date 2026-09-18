package com.ubuntu.ubuntu_app.shared.chatbot.normalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UnicodeTextNormalizerTest {

    private final TextNormalizer normalizer = new UnicodeTextNormalizer(Set.of("de", "la", "el", "en"));

    @Test
    void normalizeStripsDiacriticsAndPunctuation() {
        List<String> tokens = normalizer.normalize("¿Cómo estás? ¡Inversión en la plataforma!");
        assertEquals(List.of("como", "estas", "inversion", "plataforma"), tokens);
    }

    @Test
    void normalizeRemovesStopWords() {
        List<String> tokens = normalizer.normalize("el registro de la empresa");
        assertEquals(List.of("registro", "empresa"), tokens);
    }

    @Test
    void normalizeHandlesNullAndBlank() {
        assertTrue(normalizer.normalize(null).isEmpty());
        assertTrue(normalizer.normalize("").isEmpty());
        assertTrue(normalizer.normalize("   ").isEmpty());
    }

    @Test
    void normalizeCollapsesExtraWhitespace() {
        List<String> tokens = normalizer.normalize("   microemprendimiento     sostenible   ");
        assertEquals(List.of("microemprendimiento", "sostenible"), tokens);
    }
}
