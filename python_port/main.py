import sys
from typing import List, Dict, Any
from rag.orchestrator import RagOrchestrator
from rag.impl.rule_intent_detector import RuleIntentDetector
from rag.impl.heuristic_query_writer import HeuristicQueryWriter
from rag.impl.keyword_retriever import KeywordRetriever
from rag.impl.simple_reranker import SimpleReranker
from rag.impl.template_answer_agent import TemplateAnswerAgent
from rag.observability.trace_bus import TraceBus
from rag.observability.jsonl_trace_sink import JsonlTraceSink

def create_chunk(doc_id: str, chunk_id: str, text: str) -> Dict[str, str]:
    return {
        "docId": doc_id,
        "chunkId": chunk_id,
        "text": text
    }

def main():
    # Start logging system
    TraceBus.register(JsonlTraceSink())

    # Get question from command line arguments
    question = "Murat Can Ganiz’in ofis numarası nedir?" # Default question
    args = sys.argv
    for i in range(len(args)):
        if args[i] == "--q" and i + 1 < len(args):
            question = args[i + 1]

    # Load settings and data (Hardcoded for now)
    
    # 1. Define intent rules
    intent_rules: Dict[str, List[str]] = {
        "StaffLookup": ["ofis", "mail", "email", "iletişim", "kimdir", "hocası"],
        "Course": ["ders", "kredi", "ön koşul", "syllabus"],
        "PolicyFAQ": ["yönerge", "sınav", "mazeret", "yönetmelik"]
    }

    # 2. Define stopwords (words to ignore)
    stop_words: List[str] = ["nedir", "kimdir", "kaçtır", "hangi", "ve", "ile", "bir", "bu", "şu"]

    # 3. Prepare dummy corpus data
    index: Dict[str, Any] = {}
    corpus: List[Dict[str, str]] = []
    
    corpus.append(create_chunk("doc1", "sec1", "Murat Can Ganiz, Bilgisayar Mühendisliği bölümünde öğretim üyesidir. Ofisi MB-342 numaralı odadadır."))
    corpus.append(create_chunk("doc2", "sec1", "CSE3063 Object Oriented Software Design dersi 3. sınıf güz dönemindedir. Dersin ön koşulu CSE1142'dir."))
    corpus.append(create_chunk("doc3", "sec1", "Mazeret sınavı, geçerli bir mazereti olan öğrenciler için düzenlenir. Mazeret hakkı yalnızca ara sınavlar için geçerlidir."))
    
    index["corpus"] = corpus

    # Initialize components (Orchestrator)
    orchestrator = RagOrchestrator(
        RuleIntentDetector(intent_rules),
        HeuristicQueryWriter(stop_words),
        KeywordRetriever(),
        SimpleReranker(),
        TemplateAnswerAgent(),
        index
    )

    # Run the system
    print(f"Question: {question}")
    answer = orchestrator.run(question)
    print(f"Result: {answer}")

if __name__ == "__main__":
    main()
