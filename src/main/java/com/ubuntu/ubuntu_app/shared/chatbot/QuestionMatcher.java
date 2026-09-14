package com.ubuntu.ubuntu_app.shared.chatbot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Stateless, framework-free text matcher.
 *
 * <p>Scores candidate questions against a user query using term-frequency
 * vectors and cosine similarity. It holds no mutable state, so a single
 * instance is safe to share across threads.
 *
 * <p>It depends on nothing but the JDK: callers supply the stop-word set,
 * so this class is decoupled from persistence, web, and configuration code.
 */
public class QuestionMatcher {

    private final Set<String> stopWords;

    public QuestionMatcher(Set<String> stopWords) {
        this.stopWords = stopWords == null ? Set.of() : Set.copyOf(stopWords);
    }

    /** A candidate question with the answer it maps to. */
    public record Candidate(String question, String answer) {
    }

    /** The winning answer together with its similarity score. */
    public record Match(String answer, double score) {
    }

    /**
     * Returns the highest-scoring candidate at or above the threshold.
     *
     * @param query user input, may be null or blank (yields empty result)
     * @param candidates questions to compare against
     * @param threshold minimum score in [0.0, 1.0]
     * @return the best match, or empty when nothing reaches the threshold
     */
    public Optional<Match> bestMatch(String query, List<Candidate> candidates, double threshold) {
        if (query == null || query.isBlank() || candidates == null || candidates.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Integer> queryVector = toVector(tokenize(query));
        if (queryVector.isEmpty()) {
            return Optional.empty();
        }
        Match best = null;
        for (Candidate candidate : candidates) {
            if (candidate == null || candidate.question() == null) {
                continue;
            }
            double score = cosine(queryVector, toVector(tokenize(candidate.question())));
            if (score >= threshold && (best == null || score > best.score())) {
                best = new Match(candidate.answer(), score);
            }
        }
        return Optional.ofNullable(best);
    }

    private List<String> tokenize(String text) {
        String cleaned = text.toLowerCase().replaceAll("[¿?!*]", "");
        return java.util.Arrays.stream(cleaned.split("\\s+"))
                .map(String::trim)
                .filter(token -> !token.isEmpty() && !stopWords.contains(token))
                .toList();
    }

    private static Map<String, Integer> toVector(List<String> tokens) {
        Map<String, Integer> vector = new HashMap<>();
        for (String token : tokens) {
            vector.merge(token, 1, Integer::sum);
        }
        return vector;
    }

    private static double cosine(Map<String, Integer> left, Map<String, Integer> right) {
        if (left.isEmpty() || right.isEmpty()) {
            return 0.0;
        }
        Set<String> intersection = new HashSet<>(left.keySet());
        intersection.retainAll(right.keySet());
        double dot = 0.0;
        for (String term : intersection) {
            dot += (double) left.get(term) * right.get(term);
        }
        double normLeft = 0.0;
        for (int count : left.values()) {
            normLeft += (double) count * count;
        }
        double normRight = 0.0;
        for (int count : right.values()) {
            normRight += (double) count * count;
        }
        double denominator = Math.sqrt(normLeft) * Math.sqrt(normRight);
        return denominator == 0.0 ? 0.0 : dot / denominator;
    }
}
