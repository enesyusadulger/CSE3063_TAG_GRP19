from typing import List, Any, Protocol

class Observer(Protocol):
    def on_event(self, stage: str, inputs: Any, outputs: Any, duration_ms: int) -> None:
        ...

class TraceBus:
    _observers: List[Observer] = []

    @classmethod
    def register(cls, observer: Observer) -> None:
        cls._observers.append(observer)

    @classmethod
    def emit(cls, stage: str, inputs: Any, outputs: Any, duration_ms: int) -> None:
        for observer in cls._observers:
            observer.on_event(stage, inputs, outputs, duration_ms)
