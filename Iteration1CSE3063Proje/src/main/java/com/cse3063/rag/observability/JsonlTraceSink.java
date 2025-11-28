package com.cse3063.rag.observability;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JsonlTraceSink implements TraceBus.Observer {
    private final String logFilePath;

    public JsonlTraceSink() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        this.logFilePath = "logs/run-" + timestamp + ".jsonl";

        // Ensure logs directory exists
        new java.io.File("logs").mkdirs();
    }

    @Override
    public void onEvent(String stage, Object inputs, Object outputs, long durationMs) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", LocalDateTime.now().toString());
        logEntry.put("stage", stage);
        logEntry.put("inputs", inputs != null ? inputs.toString() : "null");
        logEntry.put("outputs", outputs != null ? outputs.toString() : "null");
        logEntry.put("durationMs", durationMs);

        try (PrintWriter out = new PrintWriter(new FileWriter(logFilePath, true))) {
            // Simple manual JSON construction to avoid external dependencies like
            // Jackson/Gson for now
            // In a real project, use a library. Here we keep it dependency-free as
            // requested (or implied minimal deps).
            // Actually, let's do a very basic string representation to be safe and simple.
            out.println(mapToJson(logEntry));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first)
                sb.append(", ");
            sb.append("\"").append(entry.getKey()).append("\": \"")
                    .append(entry.getValue().toString().replace("\"", "\\\"").replace("\n", " "))
                    .append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
