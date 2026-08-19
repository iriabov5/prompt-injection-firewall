## Context

См. `proposal.md` для мотивации. В проекте уже подключен `micronaut-management`, есть health endpoint и сервис анализа prompt, который возвращает decision, risk, latency и optional AI summary.

## Goals / Non-Goals

**Goals:**

- Использовать Micronaut Management и Micrometer для публикации runtime metrics.
- Инструментировать анализ prompt так, чтобы метрики отражали количество обработанных prompts, decisions, risks, latency и AI mode/outcome.
- Сохранить работу сервиса без AI provider.
- Проверить метрики unit и integration tests с русскими `@DisplayName`.

**Non-Goals:**

- Не добавлять Prometheus exporter в этом этапе.
- Не сохранять метрики в БД.
- Не менять JSON-контракт endpoints анализа.
- Не отправлять метрики во внешние системы.

## Decisions

1. Метрики будут писаться через Micrometer `MeterRegistry`, потому что Micronaut Management умеет публиковать их через standard metrics endpoint.

   Альтернатива — собственный `/metrics` controller. Он проще на первый взгляд, но хуже раскрывает Micronaut и дублирует management capabilities.

2. Instrumentation будет располагаться рядом с orchestration анализа prompt, чтобы фиксировать итоговые decision/risk/latency после всех analyzer futures.

   Альтернатива — записывать метрики в controller. Это хуже: controller видит HTTP request, но не должен знать детали batch accounting и AI outcome.

3. Batch-запрос будет учитывать каждый prompt item, а не только один HTTP request.

   Альтернатива — считать batch как один request. Это менее полезно для security observability: нагрузка firewall определяется количеством проверенных prompts.

4. AI outcome будет отдельным tag/value, чтобы heuristic mode не выглядел как ошибка.

   Альтернатива — вести только общий counter по decisions. Этого мало для диагностики: при выключенном AI нужно явно понимать, что сервис работает штатно в heuristic mode.

## Metrics Contract

| Metric | Type | Tags | Meaning |
| --- | --- | --- | --- |
| `prompt_firewall_analysis_total` | counter | `decision`, `risk`, `source`, `ai_mode`, `ai_outcome` | Количество проанализированных prompt items. Batch увеличивает счетчик на каждый item. |
| `prompt_firewall_analysis_latency` | timer | `decision`, `risk`, `source`, `ai_mode`, `ai_outcome` | Время полного анализа prompt item после завершения analyzer futures. |

Допустимые значения tags:

| Tag | Values |
| --- | --- |
| `decision` | `ALLOW`, `REVIEW`, `BLOCK` |
| `risk` | `LOW`, `MEDIUM`, `HIGH` |
| `source` | normalized request source или `unknown`, если source не передан |
| `ai_mode` | `disabled`, `enabled` |
| `ai_outcome` | `skipped`, `success`, `failed` |

`source` должен оставаться bounded: пустое значение превращается в `unknown`, слишком длинное или потенциально шумное значение нормализуется тем же ограничением, которое уже задано для request source.

## Acceptance Criteria

- После single analysis endpoint metric `prompt_firewall_analysis_total` увеличивается на 1.
- После batch analysis endpoint metric `prompt_firewall_analysis_total` увеличивается на количество items.
- Metrics endpoint позволяет получить список metrics и конкретные значения prompt firewall metrics.
- При `ai.enabled=false` метрики фиксируют `ai_mode=disabled` и `ai_outcome=skipped`.
- Существующие health и prompt analysis responses не меняют JSON-контракт.

## Risks / Trade-offs

- [Risk] Tags с большим числом значений могут привести к высокой cardinality -> Mitigation: использовать только ограниченные enum-like значения `decision`, `risk`, `source`, `ai_mode`/`ai_outcome`.
- [Risk] Latency из response и timer могут расходиться на единицы миллисекунд -> Mitigation: не требовать точного совпадения, тестировать факт записи timer.
- [Risk] Management metrics endpoint может быть выключен configuration -> Mitigation: явно включить endpoint в application configuration и проверить integration test.

## Migration Plan

1. Добавить dependency/configuration для Micronaut Micrometer.
2. Добавить instrumentation сервиса анализа prompt.
3. Включить management metrics endpoint.
4. Добавить unit и integration tests.
5. Обновить README.
6. Запустить tests, coverage verification и OpenSpec validation.
