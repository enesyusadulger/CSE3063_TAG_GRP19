from abc import ABC, abstractmethod
from enum import Enum
from typing import List, Dict, Any, Optional
from dataclasses import dataclass

class Intent(Enum):
    Registration = "Registration"
    StaffLookup = "StaffLookup"
    PolicyFAQ = "PolicyFAQ"
    Course = "Course"
    Unknown = "Unknown"

@dataclass
class Hit:
    docId: str
    chunkId: str
    text: str
    score: float

@dataclass
class Answer:
    text: str
    citations: List[str]

class IntentDetector(ABC):
    @abstractmethod
    def detect(self, question: str) -> Intent:
        pass

class QueryWriter(ABC):
    @abstractmethod
    def write(self, question: str, intent: Intent) -> List[str]:
        pass

class Retriever(ABC):
    @abstractmethod
    def retrieve(self, terms: List[str], index: Dict[str, Any]) -> List[Hit]:
        pass

class Reranker(ABC):
    @abstractmethod
    def rerank(self, query_terms: List[str], hits: List[Hit]) -> List[Hit]:
        pass

class AnswerAgent(ABC):
    @abstractmethod
    def answer(self, query_terms: List[str], top_hits: List[Hit]) -> Answer:
        pass
