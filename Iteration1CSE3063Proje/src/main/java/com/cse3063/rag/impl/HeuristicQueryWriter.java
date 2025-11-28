package com.cse3063.rag.impl;

import com.cse3063.rag.interfaces.QueryWriter;
import com.cse3063.rag.interfaces.IntentDetector.Intent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HeuristicQueryWriter implements QueryWriter {
    private final List<String> stopWords;

    public HeuristicQueryWriter(List<String> stopWords) {
        this.stopWords = stopWords.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    @Override
    public List<String> write(String question, Intent intent) {
        String[] tokens = question.toLowerCase().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").split("\\s+");
        List<String> terms = new ArrayList<>();

        for (String token : tokens) {
            if (!stopWords.contains(token) && !token.isEmpty()) {
                terms.add(token);
            }
        }

        // Add extra words based on intent (Booster)
        if (intent == Intent.StaffLookup) {
            terms.add("staff");
            terms.add("advisor");
            terms.add("office");
        }

        return terms.stream().distinct().collect(Collectors.toList());
    }
}
