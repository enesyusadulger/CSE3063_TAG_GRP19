package com.cse3063.rag.interfaces;

import java.util.List;
import com.cse3063.rag.interfaces.Retriever.Hit;

public interface AnswerAgent {
    class Answer {
        public String text;
        public List<String> citations;

        public Answer(String text, List<String> citations) {
            this.text = text;
            this.citations = citations;
        }
    }

    Answer answer(List<String> queryTerms, List<Hit> topHits);
}
