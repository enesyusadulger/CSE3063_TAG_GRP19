package com.cse3063.rag;

import com.cse3063.rag.impl.*;
import com.cse3063.rag.interfaces.IntentDetector.Intent;
import com.cse3063.rag.interfaces.Retriever.Hit;
import java.util.*;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("Running Unit Tests...");
        int passed = 0;
        int failed = 0;

        // Test 1: RuleIntentDetector
        try {
            System.out.print("Test 1 (IntentDetector): ");
            Map<String, List<String>> rules = new HashMap<>();
            rules.put("StaffLookup", Arrays.asList("ofis", "mail"));
            RuleIntentDetector detector = new RuleIntentDetector(rules);

            Intent result = detector.detect("Hocanın ofis numarası nedir?");
            if (result == Intent.StaffLookup) {
                System.out.println("PASSED");
                passed++;
            } else {
                System.out.println("FAILED (Expected StaffLookup, got " + result + ")");
                failed++;
            }
        } catch (Exception e) {
            System.out.println("FAILED with Exception: " + e.getMessage());
            failed++;
        }

        // Test 2: KeywordRetriever
        try {
            System.out.print("Test 2 (KeywordRetriever): ");
            KeywordRetriever retriever = new KeywordRetriever();
            Map<String, Object> index = new HashMap<>();
            List<Map<String, String>> corpus = new ArrayList<>();
            Map<String, String> chunk = new HashMap<>();
            chunk.put("docId", "d1");
            chunk.put("chunkId", "c1");
            chunk.put("text", "Java programlama dili nesne yönelimlidir.");
            corpus.add(chunk);
            index.put("corpus", corpus);

            List<String> terms = Arrays.asList("java", "nesne");
            List<Hit> hits = retriever.retrieve(terms, index);

            if (!hits.isEmpty() && hits.get(0).score > 0) {
                System.out.println("PASSED");
                passed++;
            } else {
                System.out.println("FAILED (Expected hits, got empty or zero score)");
                failed++;
            }
        } catch (Exception e) {
            System.out.println("FAILED with Exception: " + e.getMessage());
            failed++;
        }

        System.out.println("\nTotal Tests: " + (passed + failed));
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }
}
