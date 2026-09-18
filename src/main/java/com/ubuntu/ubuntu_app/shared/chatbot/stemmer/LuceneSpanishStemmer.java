package com.ubuntu.ubuntu_app.shared.chatbot.stemmer;

import org.tartarus.snowball.ext.SpanishStemmer;

public final class LuceneSpanishStemmer implements WordStemmer {

    private final ThreadLocal<SpanishStemmer> stemmerHolder = ThreadLocal.withInitial(SpanishStemmer::new);

    @Override
    public String stem(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        SpanishStemmer stemmer = stemmerHolder.get();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
