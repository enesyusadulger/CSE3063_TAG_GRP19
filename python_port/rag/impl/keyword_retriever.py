from typing import List, Dict, Any
from ..interfaces import Retriever, Hit

class KeywordRetriever(Retriever):
    def retrieve(self, terms: List[str], index: Dict[str, Any]) -> List[Hit]:
        all_hits: List[Hit] = []

        if "corpus" in index:
            corpus = index["corpus"]
            for chunk in corpus:
                text = chunk.get("text", "")
                score = self.calculate_score(text, terms)

                if score > 0:
                    all_hits.append(Hit(
                        docId=chunk.get("docId", ""),
                        chunkId=chunk.get("chunkId", ""),
                        text=text,
                        score=score
                    ))
        
        # Sort by score DESC, then docId, then chunkId
        all_hits.sort(key=lambda h: (-h.score, h.docId, h.chunkId))

        return all_hits[:10]

    def calculate_score(self, chunk_text: str, terms: List[str]) -> float:
        score = 0.0
        lower_text = chunk_text.lower()
        for term in terms:
            # Simple TF calculation
            count = (len(lower_text) - len(lower_text.replace(term, ""))) / len(term)
            score += count
        return score
