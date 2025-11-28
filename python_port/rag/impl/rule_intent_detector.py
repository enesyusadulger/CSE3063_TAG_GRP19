from typing import List, Dict, Optional
from ..interfaces import IntentDetector, Intent

class RuleIntentDetector(IntentDetector):
    def __init__(self, rules: Dict[str, List[str]]):
        self.rules = rules

    def detect(self, question: str) -> Intent:
        lower_question = question.lower()

        for intent in Intent:
            if intent == Intent.Unknown:
                continue
            
            keywords = self.rules.get(intent.value)
            if keywords:
                for keyword in keywords:
                    if keyword.lower() in lower_question:
                        return intent
        
        return Intent.Unknown
