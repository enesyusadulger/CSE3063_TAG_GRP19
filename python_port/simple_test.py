from typing import List, Dict, Any
from rag.impl.rule_intent_detector import RuleIntentDetector
from rag.impl.keyword_retriever import KeywordRetriever
from rag.interfaces import Intent

def main():
    print("Running Unit Tests...")
    passed = 0
    failed = 0

    # Test 1: RuleIntentDetector
    try:
        print("Test 1 (IntentDetector): ", end="")
        rules: Dict[str, List[str]] = {
            "StaffLookup": ["ofis", "mail"]
        }
        detector = RuleIntentDetector(rules)

        result = detector.detect("Hocanın ofis numarası nedir?")
        if result == Intent.StaffLookup:
            print("PASSED")
            passed += 1
        else:
            print(f"FAILED (Expected StaffLookup, got {result})")
            failed += 1
    except Exception as e:
        print(f"FAILED with Exception: {e}")
        failed += 1

    # Test 2: KeywordRetriever
    try:
        print("Test 2 (KeywordRetriever): ", end="")
        retriever = KeywordRetriever()
        index: Dict[str, Any] = {}
        corpus: List[Dict[str, str]] = []
        
        chunk = {
            "docId": "d1",
            "chunkId": "c1",
            "text": "Java programlama dili nesne yönelimlidir."
        }
        corpus.append(chunk)
        index["corpus"] = corpus

        terms = ["java", "nesne"]
        hits = retriever.retrieve(terms, index)

        if hits and hits[0].score > 0:
            print("PASSED")
            passed += 1
        else:
            print("FAILED (Expected hits, got empty or zero score)")
            failed += 1
    except Exception as e:
        print(f"FAILED with Exception: {e}")
        failed += 1

    print(f"\nTotal Tests: {passed + failed}")
    print(f"Passed: {passed}")
    print(f"Failed: {failed}")

if __name__ == "__main__":
    main()
