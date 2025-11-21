package com.cse3063.rag;

import java.util.List;
import com.cse3063.rag.interfaces.IntentDetector.Intent;
import com.cse3063.rag.interfaces.Retriever.Hit;
import com.cse3063.rag.interfaces.AnswerAgent.Answer;

public class RagContext {
    public String question;
    public Intent intent;
    public List<String> queryTerms;
    public List<Hit> retrievedHits;
    public List<Hit> rerankedHits;
    public Answer finalAnswer;

    public RagContext(String question) {
        this.question = question;
    }
}
