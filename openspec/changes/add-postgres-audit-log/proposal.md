## Why

После появления анализа prompt и custom rules проекту нужен persistent security audit trail: кто и какие решения получал, какие reasons сработали, сколько занял анализ и использовался ли AI. Хранить это только в памяти недостаточно: история должна переживать рестарт и быть проверяемой в интеграционных тестах на реальной PostgreSQL.

Цель: добавить PostgreSQL-backed audit log для результатов анализа prompt без сохранения исходного prompt.

Не-цели:

- не хранить полный текст prompt в базе;
- не добавлять NoSQL-хранилище на этом этапе;
- не строить полноценную SIEM/alerting систему;
- не добавлять пользовательские роли поверх существующего API key baseline.

## What Changes

- Добавляется PostgreSQL в локальный Docker Compose.
- Добавляется миграция схемы audit log.
- После анализа prompt система сохраняет audit event в PostgreSQL.
- В audit event хранится hash prompt, source, score, risk, decision, reasons, latency, признак AI usage и timestamp.
- Добавляется protected API для просмотра последних audit events и простой статистики по decisions.
- Интеграционные тесты используют Testcontainers PostgreSQL.
- README получает команды запуска PostgreSQL и примеры audit API.

## Capabilities

### New Capabilities

- `audit-log`: persistent PostgreSQL audit trail результатов prompt analysis без хранения исходного prompt.

### Modified Capabilities

- `prompt-analysis`: успешный анализ prompt создает audit event после формирования итогового response.
- `micronaut-runtime`: runtime подключает PostgreSQL persistence, миграции и protected reactive audit endpoints.
- `openapi-documentation`: OpenAPI specification описывает audit endpoints и DTO.
- `test-coverage`: integration tests для persistence используют Testcontainers PostgreSQL.

## Impact

- Gradle dependencies: Micronaut Data JDBC, JDBC pool, PostgreSQL driver, Flyway, Testcontainers PostgreSQL.
- Docker Compose: PostgreSQL service для локального запуска.
- Application configuration: datasource, Flyway, audit log settings.
- Новые entity/repository/service/controller/DTO.
- Unit tests для hash/privacy/statistics и integration tests с PostgreSQL.
