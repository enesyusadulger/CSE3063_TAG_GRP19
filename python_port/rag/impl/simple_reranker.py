from typing import List
from ..interfaces import Reranker, Hit

class SimpleReranker(Reranker):
    def rerank(self, query_terms: List[str], hits: List[Hit]) -> List[Hit]:
        for hit in hits:
            proximity_bonus = self.calculate_proximity_bonus(hit.text, query_terms)
            title_boost = self.calculate_title_boost(hit.docId, query_terms)
            
            hit.score = (hit.score * 10) + proximity_bonus + title_boost
        
        # Sort by score DESC
        hits.sort(key=lambda h: h.score, reverse=True)
        return hits

    def calculate_proximity_bonus(self, text: str, terms: List[str]) -> float:
        # Simplified implementation as per Java code
        return 0.0

    def calculate_title_boost(self, title: str, terms: List[str]) -> float:
        lower_title = title.lower()
        for term in terms:
            if term in lower_title:
                return 3.0
        return 0.0
