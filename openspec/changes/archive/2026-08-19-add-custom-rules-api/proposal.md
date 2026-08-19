## Why

Сервис уже умеет находить типовые prompt injection сигналы, но реальные проекты часто имеют собственные запретные фразы, внутренние идентификаторы, product-specific правила и compliance-триггеры. Custom rules API позволит адаптировать firewall под конкретное приложение без пересборки и без изменения встроенных эвристик.

Цель: добавить in-memory API для управления пользовательскими правилами и учитывать эти правила в анализе prompt.

Не-цели:

- не добавлять базу данных или durable storage;
- не добавлять роли пользователей и отдельную админскую модель доступа;
- не делать полноценный rule engine с собственным языком правил;
- не менять существующую модель решений `ALLOW`, `REVIEW`, `BLOCK`.

## What Changes

- Добавляется API для создания, просмотра и удаления пользовательских правил.
- Правила хранятся в памяти процесса и сбрасываются при рестарте приложения.
- Поддерживаются правила по exact phrase и regex pattern.
- Каждое правило имеет `code`, `description`, `weight`, `enabled`.
- Анализ prompt учитывает включенные custom rules и добавляет соответствующие `RiskSignal`.
- API управления правилами защищается тем же API key security baseline.
- OpenAPI specification описывает новые endpoints и DTO.
- В реализации осознанно используются Kotlin extension functions для matching/conversion и lambda with receiver для компактного rule builder/test fixtures.

## Capabilities

### New Capabilities

- `custom-rules`: управление пользовательскими in-memory правилами и применение этих правил при анализе prompt.

### Modified Capabilities

- `prompt-analysis`: prompt analysis учитывает custom rule signals вместе со встроенными эвристическими и AI-assisted signals.
- `micronaut-runtime`: Micronaut runtime публикует endpoints управления custom rules и биндингует настройки лимитов правил.
- `openapi-documentation`: OpenAPI specification описывает custom rules endpoints, request schemas и response schemas.

## Impact

- Новые controller, service/registry, DTO и analyzer для custom rules.
- Новые configuration properties для лимитов custom rules.
- Новые unit tests для matching, registry и DSL/builder.
- Новые Micronaut integration tests для HTTP API, security и влияния custom rules на анализ prompt.
- Обновление README с примерами управления правилами.
