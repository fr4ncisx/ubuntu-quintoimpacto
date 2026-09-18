package com.ubuntu.ubuntu_app.shared.chatbot.scorer;

import java.util.List;

public interface SimilarityScorer {
    double score(
            List<String> queryStems,
            List<String> candidateStems,
            List<String> queryRaw,
            List<String> candidateRaw
    );
}
