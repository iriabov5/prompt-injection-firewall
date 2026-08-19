# Bootstrap MVP Design

## Application Shape

Приложение представляет собой один Micronaut-сервис на Kotlin.

Micronaut отвечает за runtime shell:

- HTTP routing;
- validation;
- dependency injection;
- configuration binding;
- conditional AI beans;
- HTTP client integration;
- health visibility;
- integration testing support.

Core risk logic остается в analyzers, services и aggregators, которые можно тестировать без полного application context.

## Analyzer Pipeline

Каждый analyzer реализует `PromptRiskAnalyzer` и возвращает `CompletableFuture<List<RiskSignal>>`.

`PromptFirewallService` получает все analyzers через DI, запускает их конкурентно, применяет timeouts, обрабатывает failures и передает собранные signals в `RiskAggregator`.

## AI Integration

AI analysis опционален и скрыт за `AiClient`.

Первая реализация использует OpenAI-compatible chat completions API через Micronaut HTTP client. Bean `AiPromptAnalyzer` существует только при `ai.enabled=true`.

## Risk Scoring

Analyzer signals имеют явные weights. Aggregator суммирует и нормализует score в `0..100`, маппит score в risk level, затем risk level в decision.

## Testing

Большинство tests должны быть unit tests для analyzers и aggregation. Integration tests фокусируются на Micronaut wiring, validation, configuration, conditional beans и controller behavior.
