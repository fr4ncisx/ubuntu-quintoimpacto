package com.ubuntu.ubuntu_app.shared.chatbot.scorer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ubuntu.ubuntu_app.shared.chatbot.normalizer.UnicodeTextNormalizer;
import com.ubuntu.ubuntu_app.shared.chatbot.stemmer.LuceneSpanishStemmer;
import com.ubuntu.ubuntu_app.shared.support.StopWords;

class HybridSimilarityScorerTest {

    private HybridSimilarityScorer scorer;
    private UnicodeTextNormalizer normalizer;
    private LuceneSpanishStemmer stemmer;

    @BeforeEach
    void setUp() {
        this.scorer = new HybridSimilarityScorer();
        this.normalizer = new UnicodeTextNormalizer(StopWords.getLatinAmericanSpanishStopWords());
        this.stemmer = new LuceneSpanishStemmer();
    }

    private double evaluate(String query, String candidate) {
        List<String> qRaw = normalizer.normalize(query);
        List<String> cRaw = normalizer.normalize(candidate);
        List<String> qStems = qRaw.stream().map(stemmer::stem).toList();
        List<String> cStems = cRaw.stream().map(stemmer::stem).toList();
        return scorer.score(qStems, cStems, qRaw, cRaw);
    }

    @Test
    void exactMatchYieldsMaxScore() {
        double score = evaluate("como invertir en un microemprendimiento", "como invertir en un microemprendimiento");
        assertEquals(1.0, score, 0.001);
    }

    @Test
    void coherentParaphrasePassesThreshold() {
        double score = evaluate("como puedo registrarme en la plataforma", "como registrarse en la plataforma");
        assertTrue(score >= 0.50, "Score was: " + score);
    }

    @Test
    void questionWithDiacriticsAndPunctuationMatches() {
        double score = evaluate("¿Cómo invertir?", "como invertir");
        assertTrue(score >= 0.85, "Score was: " + score);
    }

    @Test
    void institutionalQueryMatches() {
        double score = evaluate("¿Quiénes somos?", "quienes somos");
        assertTrue(score >= 0.85, "Score was: " + score);
    }

    @Test
    void totalAlienQueryYieldsZero() {
        double score = evaluate("receta para cocinar pizza de muzarella", "como registrarse en la plataforma");
        assertEquals(0.0, score, 0.001);
    }

    @Test
    void boundaryQueryRegistrarAutoDoesNotMatchRegistrar() {
        double score = evaluate("quiero registrar un auto 0km en el registro automotor", "registrar");
        assertTrue(score < 0.40, "Score for alien context was too high: " + score);
    }

    @Test
    void boundaryQueryInversionBitcoinDoesNotMatchMicroemprendimiento() {
        double score = evaluate("inversion en criptomonedas y bitcoin", "como invertir en un microemprendimiento");
        assertTrue(score < 0.45, "Score for alien crypto query was too high: " + score);
    }

    @Test
    void boundaryQueryContactoOvniDoesNotMatchContacto() {
        double score = evaluate("contacto extraterrestre ovni en el cielo", "contacto");
        assertTrue(score < 0.40, "Score for alien contact was too high: " + score);
    }

    @Test
    void boundaryQueryBuscarTrabajoFabricaDoesNotMatchBuscar() {
        double score = evaluate("quiero buscar trabajo en una fabrica textil", "buscar");
        assertTrue(score < 0.40, "Score for job search was too high: " + score);
    }

    @Test
    void boundaryQueryAyudaMatematicasDoesNotMatchAyuda() {
        double score = evaluate("necesito ayuda con mi tarea de matematicas", "ayuda");
        assertTrue(score < 0.40, "Score for school help was too high: " + score);
    }
}
