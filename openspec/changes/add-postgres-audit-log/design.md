## Overview

Добавляем persistent audit subsystem: после успешного анализа prompt сервис формирует audit metadata и сохраняет event в PostgreSQL. Исходный prompt не хранится, вместо него сохраняется deterministic SHA-256 hash. Audit API позволяет получить последние events и aggregate statistics.

## Persistence

- PostgreSQL как единственная база на этом этапе.
- Flyway migration создает таблицу `prompt_analysis_audit_events`.
- Micronaut Data JDBC используется для repository layer: для этой фичи важнее простота и надежность, чем полная reactive database stack.
- HTTP boundary остается Reactor `Mono`/`Flux`; blocking repository calls выполняются из service layer через bounded async execution, чтобы не ломать style API.

## Data Model

Audit event stores:

- generated id;
- `prompt_hash`;
- `source`;
- `score`;
- `risk`;
- `decision`;
- `reasons`;
- `latency_ms`;
- `ai_used`;
- `created_at`.

`reasons` можно хранить как JSON/text column или join-free serialized value. Для первого этапа допустим JSONB/text в одной таблице, потому что запросы нужны по latest events и aggregate decisions, а не по каждому reason.

## API

- `GET /api/v1/audit/events?limit=50` — последние events newest-first.
- `GET /api/v1/audit/stats` — total и counts по decisions.

Оба endpoints protected API key и возвращают Reactor types.

## Failure Handling

Audit write failure не должен ломать клиентский analysis response. Ошибка логируется без prompt text. Это делает audit best-effort для runtime availability, но не скрывает проблему от logs/observability.

## Testing

- Unit tests: prompt hash, event mapper, stats aggregation.
- Integration tests: Testcontainers PostgreSQL + Flyway migration + repository save/query.
- Controller integration tests: protected access, latest events, stats.
- Analysis integration test: prompt analysis creates audit event.

## Alternatives

- R2DBC: более реактивно, но больше настройки и рисков для текущего размера проекта.
- NoSQL: не нужен для append-only audit trail с простыми запросами.
- In-memory history: проще, но не решает persistence и investigation use case.
