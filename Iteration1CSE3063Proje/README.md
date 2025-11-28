# CSE3063 Term Project - Iteration 1 (RAG Chatbot)

## Group Info
**Group ID:** CSE3063F25GrpX (Replace X with your group number)

## Project Overview
This project implements a modular, CLI-based Retrieval-Augmented Generation (RAG) chatbot for the Computer Engineering department. It is designed using **SOLID principles** and **GRASP patterns** to ensure high code quality and maintainability.

## Features
- **Intent Detection:** Classifies user questions (e.g., Staff Lookup, Course Info).
- **Retrieval:** Finds relevant documents using keyword matching.
- **Reranking:** Reorders results based on relevance scoring.
- **Answer Generation:** Composes answers with citations.
- **Observability:** Logs all pipeline steps to JSONL files for traceability.

## Architecture
The system uses a sequential pipeline orchestrated by a `RagOrchestrator` (Controller). Key design patterns include:
- **Strategy Pattern:** For interchangeable components (`IntentDetector`, `Retriever`, etc.).
- **Template Method:** For defining the pipeline execution flow.
- **Observer Pattern:** For logging events via `TraceBus`.

## How to Run
1.  **Compile:**
    ```bash
    javac -encoding UTF-8 -d bin src/main/java/com/cse3063/rag/*.java src/main/java/com/cse3063/rag/impl/*.java src/main/java/com/cse3063/rag/interfaces/*.java src/main/java/com/cse3063/rag/observability/*.java
    ```
    *(Note: Adjust paths if not using standard src structure, or simply compile from the root of source)*

    **Simple Compile (if all in one folder structure):**
    ```bash
    javac -encoding UTF-8 com/cse3063/rag/Main.java
    ```

2.  **Run:**
    ```bash
    java -Dfile.encoding=UTF-8 com.cse3063.rag.Main --q "Soru buraya"
    ```

## Project Structure
- `src/main/java/com/cse3063/rag/interfaces`: Core interfaces (Strategy definitions).
- `src/main/java/com/cse3063/rag/impl`: Concrete implementations of strategies.
- `src/main/java/com/cse3063/rag/observability`: Logging infrastructure.
- `src/main/java/com/cse3063/rag/RagOrchestrator.java`: Main controller.
