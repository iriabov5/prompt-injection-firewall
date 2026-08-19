## ADDED Requirements

### Requirement: Swagger UI route

Micronaut runtime SHALL публиковать route или static resources для Swagger UI без влияния на core prompt analysis lifecycle.

#### Scenario: Swagger UI route не запускает анализ prompt

- **GIVEN** приложение запущено
- **WHEN** developer открывает Swagger UI route
- **THEN** Micronaut SHALL вернуть documentation UI
- **AND** prompt analysis SHALL NOT запускаться
