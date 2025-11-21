package com.cse3063.rag;

import com.cse3063.rag.impl.*;
import com.cse3063.rag.observability.JsonlTraceSink;
import com.cse3063.rag.observability.TraceBus;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Start logging system
        TraceBus.register(new JsonlTraceSink());

        // Get question from command line arguments
        String question = "Murat Can Ganiz’in ofis numarası nedir?"; // Default question
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--q") && i + 1 < args.length) {
                question = args[i + 1];
            }
        }

        // Load settings and data (Hardcoded for now)
        // Normalde YAML dosyasindan okumak lazim ama boyle daha basit

        // 1. Define intent rules
        Map<String, List<String>> intentRules = new HashMap<>();
        intentRules.put("StaffLookup", Arrays.asList("ofis", "mail", "email", "iletişim", "kimdir", "hocası"));
        intentRules.put("Course", Arrays.asList("ders", "kredi", "ön koşul", "syllabus"));
        intentRules.put("PolicyFAQ", Arrays.asList("yönerge", "sınav", "mazeret", "yönetmelik"));

        // 2. Define stopwords (words to ignore)
        List<String> stopWords = Arrays.asList("nedir", "kimdir", "kaçtır", "hangi", "ve", "ile", "bir", "bu", "şu");

        // 3. Prepare dummy corpus data
        // We add manually because we don't read from file yet
        Map<String, Object> index = new HashMap<>();

        List<Map<String, String>> corpus = new ArrayList<>();
        // Initialize components (Orchestrator)
        RagOrchestrator orchestrator = new RagOrchestrator(
                new RuleIntentDetector(intentRules),
                new HeuristicQueryWriter(stopWords),
                new KeywordRetriever(), // Pass corpus map here
                new SimpleReranker(),
                new TemplateAnswerAgent(),
                index);

        // Run the system
        System.out.println("Question: " + question);
        String answer = orchestrator.run(question);
        System.out.println("Result: " + answer);
    }

    private static Map<String, String> createChunk(String docId, String chunkId, String text) {
        Map<String, String> chunk = new HashMap<>();
        chunk.put("docId", docId);
        chunk.put("chunkId", chunkId);
        chunk.put("text", text);
        return chunk;
    }
}
