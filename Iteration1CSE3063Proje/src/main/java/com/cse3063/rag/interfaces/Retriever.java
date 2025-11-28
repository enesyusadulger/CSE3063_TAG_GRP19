package com.cse3063.rag.interfaces;

import java.util.List;
import java.util.Map;

public interface Retriever {
    // Hit represents a retrieved chunk with metadata
    class Hit {
        public String docId;
        public String chunkId;
        public String text;
        public double score;
        
        public Hit(String docId, String chunkId, String text, double score) {
            this.docId = docId;
            this.chunkId = chunkId;
            this.text = text;
            this.score = score;
        }
    }

    List<Hit> retrieve(List<String> terms, Map<String, Object> index);
}
