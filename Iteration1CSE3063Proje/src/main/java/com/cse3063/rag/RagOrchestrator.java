package com.cse3063.rag;

import com.cse3063.rag.interfaces.*;
import com.cse3063.rag.observability.TraceBus;
import java.util.Map;

public class RagOrchestrator {
    private final IntentDetector intentDetector;
    private final QueryWriter queryWriter;
    private final Retriever retriever;
    private final Reranker reranker;
    private final AnswerAgent answerAgent;
    private final Map<String, Object> index;

    public RagOrchestrator(IntentDetector intentDetector, QueryWriter queryWriter,
            Retriever retriever, Reranker reranker,
            AnswerAgent answerAgent, Map<String, Object> index) {
        this.intentDetector = intentDetector;
        this.queryWriter = queryWriter;
        this.retriever = retriever;
        this.reranker = reranker;
        this.answerAgent = answerAgent;
        this.index = index;
    }

    public String run(String question) {
        RagContext context = new RagContext(question);

        // Call steps in order (Template Method)
        detectIntent(context);
        writeQuery(context);
        retrieve(context);
        rerank(context);
        answer(context);

        return context.finalAnswer.text + " [Citations: " + context.finalAnswer.citations + "]";
    }

    private void detectIntent(RagContext context) {
        long start = System.currentTimeMillis();
        context.intent = intentDetector.detect(context.question);
        TraceBus.emit("IntentDetection", context.question, context.intent, System.currentTimeMillis() - start);
    }

    private void writeQuery(RagContext context) {
        long start = System.currentTimeMillis();
        context.queryTerms = queryWriter.write(context.question, context.intent);
        TraceBus.emit("QueryWriting", context.intent, context.queryTerms, System.currentTimeMillis() - start);
    }

    private void retrieve(RagContext context) {
        long start = System.currentTimeMillis();
        context.retrievedHits = retriever.retrieve(context.queryTerms, index);
        TraceBus.emit("Retrieval", context.queryTerms, context.retrievedHits.size() + " hits",
                System.currentTimeMillis() - start);
    }

    private void rerank(RagContext context) {
        long start = System.currentTimeMillis();
        context.rerankedHits = reranker.rerank(context.queryTerms, context.retrievedHits);
        TraceBus.emit("Reranking", context.retrievedHits.size() + " hits",
                "Top score: " + (context.rerankedHits.isEmpty() ? "0" : context.rerankedHits.get(0).score),
                System.currentTimeMillis() - start);
    }

    private void answer(RagContext context) {
        long start = System.currentTimeMillis();
        context.finalAnswer = answerAgent.answer(context.queryTerms, context.rerankedHits);
        TraceBus.emit("AnswerGeneration", context.rerankedHits.size() + " hits", context.finalAnswer.text,
                System.currentTimeMillis() - start);
    }
}
