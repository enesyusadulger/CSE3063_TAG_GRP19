import os
import json
from datetime import datetime
from typing import Any, Dict
from .trace_bus import Observer

class JsonlTraceSink(Observer):
    def __init__(self):
        timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")
        self.log_file_path = f"logs/run-{timestamp}.jsonl"
        
        if not os.path.exists("logs"):
            os.makedirs("logs")

    def on_event(self, stage: str, inputs: Any, outputs: Any, duration_ms: int) -> None:
        log_entry: Dict[str, Any] = {
            "timestamp": str(datetime.now()),
            "stage": stage,
            "inputs": str(inputs),
            "outputs": str(outputs),
            "durationMs": duration_ms
        }

        with open(self.log_file_path, "a", encoding="utf-8") as f:
            f.write(json.dumps(log_entry) + "\n")
