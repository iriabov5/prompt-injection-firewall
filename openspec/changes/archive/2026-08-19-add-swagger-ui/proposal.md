## Why

OpenAPI specification уже дает машинно-читаемый контракт API, но для публичного проекта неудобно каждый раз читать YAML или дергать endpoints через curl. Swagger UI добавит быстрый browser-based способ посмотреть API, схемы запросов и ответов без изменения поведения firewall.

Цель изменения — сделать API проще для изучения и ручной проверки. Не-цели: менять public API, переписывать OpenAPI generation, добавлять AI provider или делать Swagger UI source of truth.

## What Changes

- Добавляется Swagger UI как отдельная browser-based capability для просмотра public HTTP API.
- Swagger UI будет использовать актуальную OpenAPI specification, сгенерированную из Micronaut controllers и DTO metadata.
- Micronaut runtime будет публиковать route/static resources для Swagger UI.
- README будет дополнен адресом Swagger UI и короткой командой запуска.
- Поведение prompt analysis endpoints, risk scoring и AI provider configuration не меняется.

## Capabilities

### New Capabilities

- `swagger-ui`: интерактивная browser-based документация public API поверх сгенерированной OpenAPI specification.

### Modified Capabilities

- `openapi-documentation`: OpenAPI specification должна быть пригодна для использования Swagger UI, оставаясь source of truth для UI.
- `micronaut-runtime`: runtime должен публиковать Swagger UI route/static resources без влияния на core prompt analysis behavior.

## Impact

- Gradle dependencies или Micronaut Swagger UI/static resources configuration.
- Micronaut application configuration для маршрута Swagger UI и пути к OpenAPI specification.
- Интеграционные тесты доступности Swagger UI и связи с generated OpenAPI specification.
- README с адресами OpenAPI YAML и Swagger UI.
