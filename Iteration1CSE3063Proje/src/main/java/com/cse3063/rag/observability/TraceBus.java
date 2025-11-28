package com.cse3063.rag.observability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TraceBus {
    public interface Observer {
        void onEvent(String stage, Object inputs, Object outputs, long durationMs);
    }

    private static final List<Observer> observers = new ArrayList<>();

    public static void register(Observer observer) {
        observers.add(observer);
    }

    public static void emit(String stage, Object inputs, Object outputs, long durationMs) {
        for (Observer observer : observers) {
            observer.onEvent(stage, inputs, outputs, durationMs);
        }
    }
}
