package com.ubuntu.ubuntu_app.shared.chatbot.scorer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.text.similarity.LevenshteinDistance;

public final class HybridSimilarityScorer implements SimilarityScorer {

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    @Override
    public double score(
            List<String> queryStems,
            List<String> candidateStems,
            List<String> queryRaw,
            List<String> candidateRaw
    ) {
        if (queryStems == null || candidateStems == null || queryStems.isEmpty() || candidateStems.isEmpty()) {
            return 0.0;
        }

        int matchedTokens = countMatches(queryStems, candidateStems, queryRaw, candidateRaw);
        if (matchedTokens == 0) {
            return 0.0;
        }

        double precision = (double) matchedTokens / queryStems.size();
        double recall = (double) matchedTokens / candidateStems.size();

        Map<String, Integer> queryVector = toFrequencyVector(queryStems);
        Map<String, Integer> candidateVector = toFrequencyVector(candidateStems);
        double cosine = calculateCosine(queryVector, candidateVector);

        double dice = (2.0 * matchedTokens) / (queryStems.size() + candidateStems.size());

        if (precision >= 1.0 && recall >= 1.0) {
            return Math.max(0.85, cosine);
        }

        if (precision >= 1.0 && candidateStems.size() >= queryStems.size()) {
            return Math.max(0.85, cosine);
        }

        double semanticFactor = Math.sqrt(precision);
        return cosine * dice * semanticFactor;
    }

    private int countMatches(
            List<String> queryStems,
            List<String> candidateStems,
            List<String> queryRaw,
            List<String> candidateRaw
    ) {
        int matches = 0;
        for (int i = 0; i < queryStems.size(); i++) {
            String qStem = queryStems.get(i);
            String qRaw = (queryRaw != null && i < queryRaw.size()) ? queryRaw.get(i) : qStem;

            for (int j = 0; j < candidateStems.size(); j++) {
                String cStem = candidateStems.get(j);
                String cRaw = (candidateRaw != null && j < candidateRaw.size()) ? candidateRaw.get(j) : cStem;

                if (isMatch(qStem, qRaw, cStem, cRaw)) {
                    matches++;
                    break;
                }
            }
        }
        return matches;
    }

    private boolean isMatch(String sStem, String sRaw, String tStem, String tRaw) {
        if (sStem.equals(tStem) || sRaw.equals(tRaw)) {
            return true;
        }
        if (sRaw.length() > 4 && tRaw.length() > 4 && levenshtein.apply(sRaw, tRaw) <= 1) {
            return true;
        }
        return sStem.length() > 3 && tStem.length() > 3 && levenshtein.apply(sStem, tStem) <= 1;
    }

    private static Map<String, Integer> toFrequencyVector(List<String> tokens) {
        Map<String, Integer> vector = new HashMap<>(tokens.size());
        for (String token : tokens) {
            vector.merge(token, 1, Integer::sum);
        }
        return vector;
    }

    private static double calculateCosine(Map<String, Integer> left, Map<String, Integer> right) {
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
