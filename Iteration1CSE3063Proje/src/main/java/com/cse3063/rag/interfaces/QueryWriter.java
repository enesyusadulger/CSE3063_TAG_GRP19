package com.cse3063.rag.interfaces;

import java.util.List;
import com.cse3063.rag.interfaces.IntentDetector.Intent;

public interface QueryWriter {
    List<String> write(String question, Intent intent);
}
