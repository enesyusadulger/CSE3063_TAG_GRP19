package com.cse3063.rag.impl;

import com.cse3063.rag.interfaces.Retriever;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeywordRetriever implements Retriever {

    @Override
    public List<Hit> retrieve(List<String> terms, Map<String, Object> index) {
        List<Hit> allHits = new ArrayList<>();

        // Get corpus list from index map
        if (index.containsKey("corpus")) {
            List<Map<String, String>> corpus = (List<Map<String, String>>) index.get("corpus");

            for (Map<String, String> chunk : corpus) {
                String text = chunk.get("text");
                double score = calculateScore(text, terms);

                if (score > 0) {
                    allHits.add(new Hit(
                            chunk.get("docId"),
                            chunk.get("chunkId"),
                            text,
                            score));
                }
            }
        }

        // Sort by score (Highest score first)
        // Eger puanlar esitse id'ye gore sirala
        allHits.sort((h1, h2) -> {
            int scoreCompare = Double.compare(h2.score, h1.score);
            if (scoreCompare != 0)
                return scoreCompare;

            int docCompare = h1.docId.compareTo(h2.docId);
            if (docCompare != 0)
                return docCompare;

            return h1.chunkId.compareTo(h2.chunkId);
        });

        // Return top 10 results
        return allHits.size() > 10 ? allHits.subList(0, 10) : allHits;
    }

    // Helper function to calculate score
    public double calculateScore(String chunkText, List<String> terms) {
        double score = 0;
        String lowerText = chunkText.toLowerCase();
        for (String term : terms) {
            // Count how many times word appears
            // Simple TF calculation
            int count = (lowerText.length() - lowerText.replace(term, "").length()) / term.length();
            score += count;
        }
        return score;
    }
}
