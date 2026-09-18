package com.ubuntu.ubuntu_app.shared.chatbot;

import com.ubuntu.ubuntu_app.shared.chatbot.normalizer.TextNormalizer;
import com.ubuntu.ubuntu_app.shared.chatbot.normalizer.UnicodeTextNormalizer;
import com.ubuntu.ubuntu_app.shared.chatbot.scorer.HybridSimilarityScorer;
import com.ubuntu.ubuntu_app.shared.chatbot.scorer.SimilarityScorer;
import com.ubuntu.ubuntu_app.shared.chatbot.stemmer.LuceneSpanishStemmer;
import com.ubuntu.ubuntu_app.shared.chatbot.stemmer.WordStemmer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class QuestionMatcher {

    private final TextNormalizer normalizer;
    private final WordStemmer stemmer;
    private final SimilarityScorer scorer;

    public QuestionMatcher(Set<String> stopWords) {
        this(new UnicodeTextNormalizer(stopWords), new LuceneSpanishStemmer(), new HybridSimilarityScorer());
    }

    public QuestionMatcher(TextNormalizer normalizer, WordStemmer stemmer, SimilarityScorer scorer) {
        this.normalizer = Objects.requireNonNull(normalizer);
        this.stemmer = Objects.requireNonNull(stemmer);
        this.scorer = Objects.requireNonNull(scorer);
    }

    public record Candidate(String question, String answer) {
    }

    public record Match(String answer, double score) {
    }

    private record ProcessedText(List<String> rawTokens, List<String> stemTokens) {
    }

    public Optional<Match> bestMatch(String query, List<Candidate> candidates, double threshold) {
        if (query == null || query.isBlank() || candidates == null || candidates.isEmpty()) {
            return Optional.empty();
        }

        ProcessedText queryProcessed = processText(query);
        if (queryProcessed.stemTokens().isEmpty()) {
            return Optional.empty();
        }

        Match best = null;
        for (Candidate candidate : candidates) {
            if (candidate == null || candidate.question() == null) {
                continue;
            }
            ProcessedText candidateProcessed = processText(candidate.question());
            double score = scorer.score(
                    queryProcessed.stemTokens(),
                    candidateProcessed.stemTokens(),
                    queryProcessed.rawTokens(),
                    candidateProcessed.rawTokens()
            );

            if (score >= threshold && (best == null || score > best.score())) {
                best = new Match(candidate.answer(), score);
            }
        }
        return Optional.ofNullable(best);
    }

    private ProcessedText processText(String text) {
        List<String> raw = normalizer.normalize(text);
        List<String> stems = raw.stream()
                .map(stemmer::stem)
                .filter(stem -> !stem.isEmpty())
                .toList();
        return new ProcessedText(raw, stems);
    }
}
