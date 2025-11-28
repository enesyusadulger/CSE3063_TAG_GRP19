package com.cse3063.rag.interfaces;

import java.util.List;
import java.util.Map;

public interface IntentDetector {
    enum Intent {
        Registration, StaffLookup, PolicyFAQ, Course, Unknown
    }

    Intent detect(String question);
}
