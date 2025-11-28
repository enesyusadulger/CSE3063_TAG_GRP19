from typing import List, Optional
from .interfaces import Intent, Hit, Answer

class RagContext:
    def __init__(self, question: str):
        self.question: str = question
        self.intent: Optional[Intent] = None
        self.query_terms: List[str] = []
        self.retrieved_hits: List[Hit] = []
        self.reranked_hits: List[Hit] = []
        self.final_answer: Optional[Answer] = None
