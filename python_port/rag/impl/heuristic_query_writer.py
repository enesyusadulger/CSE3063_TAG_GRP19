import re
from typing import List
from ..interfaces import QueryWriter, Intent

class HeuristicQueryWriter(QueryWriter):
    def __init__(self, stop_words: List[str]):
        self.stop_words = [word.lower() for word in stop_words]

    def write(self, question: str, intent: Intent) -> List[str]:
        # Remove non-alphanumeric characters (keeping Turkish chars and spaces)
        cleaned = re.sub(r"[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "", question.lower())
        tokens = cleaned.split()
        
        terms = []
        for token in tokens:
            if token not in self.stop_words and token:
                terms.append(token)
        
        # Add extra words based on intent (Booster)
        if intent == Intent.StaffLookup:
            terms.extend(["staff", "advisor", "office"])
        
        # Return distinct terms while preserving order
        return list(dict.fromkeys(terms))
