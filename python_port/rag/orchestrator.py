import time
from typing import Dict, Any
from .context import RagContext
from .interfaces import IntentDetector, QueryWriter, Retriever, Reranker, AnswerAgent
from .observability.trace_bus import TraceBus

class RagOrchestrator:
    def __init__(self, 
                 intent_detector: IntentDetector,
                 query_writer: QueryWriter,
                 retriever: Retriever,
                 reranker: Reranker,
                 answer_agent: AnswerAgent,
                 index: Dict[str, Any]):
        self.intent_detector = intent_detector
        self.query_writer = query_writer
        self.retriever = retriever
        self.reranker = reranker
        self.answer_agent = answer_agent
        self.index = index

    def run(self, question: str) -> str:
        context = RagContext(question)

        # Call steps in order (Template Method)
        self.detect_intent(context)
        self.write_query(context)
        self.retrieve(context)
        self.rerank(context)
        self.answer(context)

        if context.final_answer:
            return f"{context.final_answer.text} [Citations: {context.final_answer.citations}]"
        return "No answer generated."

    def detect_intent(self, context: RagContext) -> None:
        start = time.time() * 1000
        context.intent = self.intent_detector.detect(context.question)
        duration = int((time.time() * 1000) - start)
        TraceBus.emit("IntentDetection", context.question, str(context.intent), duration)

    def write_query(self, context: RagContext) -> None:
        start = time.time() * 1000
        if context.intent:
            context.query_terms = self.query_writer.write(context.question, context.intent)
        duration = int((time.time() * 1000) - start)
        TraceBus.emit("QueryWriting", str(context.intent), str(context.query_terms), duration)

    def retrieve(self, context: RagContext) -> None:
        start = time.time() * 1000
        context.retrieved_hits = self.retriever.retrieve(context.query_terms, self.index)
        duration = int((time.time() * 1000) - start)
        TraceBus.emit("Retrieval", str(context.query_terms), f"{len(context.retrieved_hits)} hits", duration)

    def rerank(self, context: RagContext) -> None:
        start = time.time() * 1000
        context.reranked_hits = self.reranker.rerank(context.query_terms, context.retrieved_hits)
        top_score = "0" if not context.reranked_hits else str(context.reranked_hits[0].score)
        duration = int((time.time() * 1000) - start)
        TraceBus.emit("Reranking", f"{len(context.retrieved_hits)} hits", f"Top score: {top_score}", duration)

    def answer(self, context: RagContext) -> None:
        start = time.time() * 1000
        context.final_answer = self.answer_agent.answer(context.query_terms, context.reranked_hits)
        duration = int((time.time() * 1000) - start)
        TraceBus.emit("AnswerGeneration", f"{len(context.reranked_hits)} hits", context.final_answer.text, duration)
