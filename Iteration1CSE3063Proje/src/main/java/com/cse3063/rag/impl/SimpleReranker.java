package com.cse3063.rag.impl;

import com.cse3063.rag.interfaces.Reranker;
import com.cse3063.rag.interfaces.Retriever.Hit;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleReranker implements Reranker {

    @Override
    public List<Hit> rerank(List<String> queryTerms, List<Hit> hits) {
        // Apply bonus scores
        for (Hit hit : hits) {
            double proximityBonus = calculateProximityBonus(hit.text, queryTerms);
            double titleBoost = calculateTitleBoost(hit.docId, queryTerms); // Simplified: check docId as title

            hit.score = (hit.score * 10) + proximityBonus + titleBoost;
        }

        // Sort by score DESC
        hits.sort((h1, h2) -> Double.compare(h2.score, h1.score));
        return hits;
    }

    private double calculateProximityBonus(String text, List<String> terms) {
        // +5 if any two query terms appear within 15 characters
        // Simplified implementation
        return 0.0;
    }

    private double calculateTitleBoost(String title, List<String> terms) {
        // +3 if doc title contains any query term
        for (String term : terms) {
            if (title.toLowerCase().contains(term)) {
                return 3.0;
            }
        }
        return 0.0;
    }
}
