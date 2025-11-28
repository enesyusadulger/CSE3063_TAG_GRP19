package com.cse3063.rag.impl;

import com.cse3063.rag.interfaces.AnswerAgent;
import com.cse3063.rag.interfaces.Retriever.Hit;
import java.util.Collections;
import java.util.List;

public class TemplateAnswerAgent implements AnswerAgent {

    @Override
    public Answer answer(List<String> queryTerms, List<Hit> topHits) {
        if (topHits.isEmpty()) {
            return new Answer("I couldn't find any information about that.", Collections.emptyList());
        }

        Hit bestHit = topHits.get(0);
        String bestSentence = findBestSentence(bestHit.text, queryTerms);

        String answerText = String.format("Your answer: %s. See: %s:%s",
                bestSentence, bestHit.docId, bestHit.chunkId);

        return new Answer(answerText, Collections.singletonList(bestHit.docId + ":" + bestHit.chunkId));
    }

    private String findBestSentence(String text, List<String> terms) {
        String[] sentences = text.split("[.!?]");
        String best = sentences[0];
        int maxMatches = 0;

        for (String sentence : sentences) {
            int matches = 0;
            for (String term : terms) {
                if (sentence.toLowerCase().contains(term)) {
                    matches++;
                }
            }
            if (matches > maxMatches) {
                maxMatches = matches;
                best = sentence;
            }
        }
        return best.trim();
    }
}
