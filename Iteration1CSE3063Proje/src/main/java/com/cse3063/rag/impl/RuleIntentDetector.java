package com.cse3063.rag.impl;

import com.cse3063.rag.interfaces.IntentDetector;
import java.util.List;
import java.util.Map;

public class RuleIntentDetector implements IntentDetector {
    private final Map<String, List<String>> rules;

    public RuleIntentDetector(Map<String, List<String>> rules) {
        this.rules = rules;
    }

    @Override
    public Intent detect(String question) {
        String lowerQuestion = question.toLowerCase();

        // Priority can be configured, iterating map for now
        for (Intent intent : Intent.values()) {
            if (intent == Intent.Unknown)
                continue;

            List<String> keywords = rules.get(intent.name());
            if (keywords != null) {
                for (String keyword : keywords) {
                    if (lowerQuestion.contains(keyword.toLowerCase())) {
                        return intent;
                    }
                }
            }
        }

        return Intent.Unknown;
    }
}
