import re
from typing import List
from ..interfaces import AnswerAgent, Hit, Answer

class TemplateAnswerAgent(AnswerAgent):
    def answer(self, query_terms: List[str], top_hits: List[Hit]) -> Answer:
        if not top_hits:
            return Answer("I couldn't find any information about that.", [])
        
        best_hit = top_hits[0]
        best_sentence = self.find_best_sentence(best_hit.text, query_terms)
        
        answer_text = f"Your answer: {best_sentence}. See: {best_hit.docId}:{best_hit.chunkId}"
        
        return Answer(answer_text, [f"{best_hit.docId}:{best_hit.chunkId}"])

    def find_best_sentence(self, text: str, terms: List[str]) -> str:
        sentences = re.split(r'[.!?]', text)
        # Filter out empty strings from split
        sentences = [s.strip() for s in sentences if s.strip()]
        
        if not sentences:
            return text # Fallback if no sentence splitting happened

        best = sentences[0]
        max_matches = 0
        
        for sentence in sentences:
            matches = 0
            lower_sentence = sentence.lower()
            for term in terms:
                if term in lower_sentence:
                    matches += 1
            
            if matches > max_matches:
                max_matches = matches
                best = sentence
                
        return best
