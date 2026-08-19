# micronaut-runtime Delta

## MODIFIED Requirements

### Requirement: HTTP API

Система SHALL публиковать операции анализа prompt через Micronaut HTTP controllers и SHALL поддерживать OpenAPI documentation для public HTTP endpoints.

#### Scenario: Analyze endpoint существует

- GIVEN приложение запущено
- WHEN клиент отправляет `POST /api/v1/prompts/analyze`
- THEN Micronaut SHALL направить запрос в prompt analysis controller

#### Scenario: OpenAPI documentation подключена к Micronaut runtime

- GIVEN public HTTP endpoints реализованы через Micronaut controllers
- WHEN OpenAPI specification генерируется
- THEN Micronaut OpenAPI integration SHALL использовать controller metadata для описания endpoints
