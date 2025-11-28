# Appendix: LLM Usage Report

## Overview
In this project (Iteration 1), we utilized an AI coding assistant (Google Deepmind Agent) to accelerate the development process, ensure adherence to SOLID principles, and generate documentation artifacts.

## Usage Areas

### 1. Architecture Design
The AI assisted in designing the class hierarchy to comply with the **Strategy Pattern**. We defined interfaces (`IntentDetector`, `Retriever`, etc.) to ensure modularity and the **Open/Closed Principle**.

### 2. Code Generation
The AI generated the initial boilerplate code for:
- **Interfaces:** Defining the contracts for each pipeline stage.
- **Implementations:** Creating baseline implementations (e.g., `KeywordRetriever`, `RuleIntentDetector`).
- **Orchestration:** Implementing the `RagOrchestrator` using the **Template Method** pattern.

### 3. Documentation & UML
The AI helped in:
- Drafting the **Requirement Analysis Document (RAD)**.
- Generating **Mermaid.js** code for:
    - Domain Model
    - Design Class Diagram (DCD)
    - System Sequence Diagram (SSD)
    - Design Sequence Diagram (DSD)

### 4. Verification
The AI guided the verification process by suggesting CLI scenarios and debugging minor syntax issues during the initial compile phase.

## Conclusion
The use of LLM tools allowed us to focus on high-level design decisions and logic while automating repetitive coding tasks and documentation formatting. All generated code was reviewed and verified by the team.
