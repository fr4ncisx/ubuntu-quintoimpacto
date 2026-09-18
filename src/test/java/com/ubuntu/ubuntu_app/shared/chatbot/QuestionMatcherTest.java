package com.ubuntu.ubuntu_app.shared.chatbot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QuestionMatcherTest {

    private static final Set<String> STOP_WORDS = Set.of(
            "el", "la", "de", "que", "en", "y", "un", "una", "los", "las", "como", "quiero", "puedo"
    );

    private final QuestionMatcher matcher = new QuestionMatcher(STOP_WORDS);

    private static List<QuestionMatcher.Candidate> candidates() {
        return List.of(
                new QuestionMatcher.Candidate("como invertir en microemprendimiento", "usa el boton contactar"),
                new QuestionMatcher.Candidate("como registrarse en la plataforma", "debes completar el formulario de registro"),
                new QuestionMatcher.Candidate("hola buenas", "hola, en que ayudo"),
                new QuestionMatcher.Candidate("que es ubuntu", "empresa de financiamiento sostenible"),
                new QuestionMatcher.Candidate("quienes somos", "empresa de financiamiento sostenible"),
                new QuestionMatcher.Candidate("registrar", "debes completar el formulario de registro"),
                new QuestionMatcher.Candidate("inversion", "usa el boton contactar"),
                new QuestionMatcher.Candidate("buscar", "explorar microemprendimientos"),
                new QuestionMatcher.Candidate("contacto", "usa el boton contactar"),
                new QuestionMatcher.Candidate("ayuda", "asistencia"));
    }

    @Test
    void exactMatchScoresOne() {
        var match = matcher.bestMatch("como invertir en microemprendimiento", candidates(), 0.5);

        assertTrue(match.isPresent());
        assertEquals("usa el boton contactar", match.get().answer());
        assertEquals(1.0, match.get().score(), 1e-9);
    }

    @Test
    void stemmingMatchesDifferentConjugations() {
        var match = matcher.bestMatch("quiero registrarme", candidates(), 0.5);

        assertTrue(match.isPresent());
        assertEquals("debes completar el formulario de registro", match.get().answer());
    }

    @Test
    void accentInsensitivityMatchesCandidate() {
        var match = matcher.bestMatch("¿cómo invertir en microemprendimiento?", candidates(), 0.5);

        assertTrue(match.isPresent());
        assertEquals("usa el boton contactar", match.get().answer());
    }

    @Test
    void minorTypoIsToleratedWithFuzzyMatching() {
        var match = matcher.bestMatch("como registrarze en la plataforma", candidates(), 0.5);

        assertTrue(match.isPresent());
        assertEquals("debes completar el formulario de registro", match.get().answer());
    }

    @Test
    void unrelatedQueryYieldsEmpty() {
        var match = matcher.bestMatch("receta de empanadas criollas", candidates(), 0.5);

        assertTrue(match.isEmpty());
    }

    @Test
    void belowThresholdYieldsEmpty() {
        var match = matcher.bestMatch("invertir", candidates(), 0.99);

        assertTrue(match.isEmpty());
    }

    @Test
    void nullOrBlankQueryYieldsEmpty() {
        assertTrue(matcher.bestMatch(null, candidates(), 0.5).isEmpty());
        assertTrue(matcher.bestMatch("   ", candidates(), 0.5).isEmpty());
    }

    @Test
    void stopWordsOnlyQueryYieldsEmpty() {
        assertTrue(matcher.bestMatch("el la de que", candidates(), 0.5).isEmpty());
    }

    @Test
    void matchingIsCaseInsensitiveAndIgnoresPunctuation() {
        var match = matcher.bestMatch("¿QUE es UBUNTU?", candidates(), 0.5);

        assertTrue(match.isPresent());
        assertEquals("empresa de financiamiento sostenible", match.get().answer());
    }

    @Test
    void sharedInstanceIsSafeUnderConcurrency() throws Exception {
        try (var pool = Executors.newFixedThreadPool(8)) {
            List<Callable<Boolean>> tasks = IntStream.range(0, 100)
                    .mapToObj(i -> (Callable<Boolean>) () -> {
                        String query = (i % 2 == 0) ? "como invertir en microemprendimiento" : "hola buenas";
                        String expected = (i % 2 == 0) ? "usa el boton contactar" : "hola, en que ayudo";
                        return matcher.bestMatch(query, candidates(), 0.5)
                                .map(m -> m.answer().equals(expected))
                                .orElse(false);
                    })
                    .toList();
            for (var future : pool.invokeAll(tasks)) {
                assertTrue(future.get());
            }
        }
    }

    @Test
    void boundaryQueriesWithAlienContextDoNotTriggerFalsePositives() {
        assertTrue(matcher.bestMatch("quiero registrar un auto 0km", candidates(), 0.5).isEmpty());
        assertTrue(matcher.bestMatch("inversion en criptomonedas y bitcoin", candidates(), 0.5).isEmpty());
        assertTrue(matcher.bestMatch("quiero buscar trabajo en una fabrica", candidates(), 0.5).isEmpty());
        assertTrue(matcher.bestMatch("contacto extraterrestre ovni", candidates(), 0.5).isEmpty());
        assertTrue(matcher.bestMatch("necesito ayuda con mi tarea de matematicas", candidates(), 0.5).isEmpty());
    }

    @Test
    void allStopWordsQueryPreservesTokensAndMatchesCandidate() {
        var match = matcher.bestMatch("¿quienes somos?", candidates(), 0.5);

        assertTrue(match.isPresent());
        assertEquals("empresa de financiamiento sostenible", match.get().answer());
    }
}
