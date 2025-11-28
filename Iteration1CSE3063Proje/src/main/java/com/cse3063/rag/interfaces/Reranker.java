package com.cse3063.rag.interfaces;

import java.util.List;
import com.cse3063.rag.interfaces.Retriever.Hit;

public interface Reranker {
    List<Hit> rerank(List<String> queryTerms, List<Hit> hits);
}
