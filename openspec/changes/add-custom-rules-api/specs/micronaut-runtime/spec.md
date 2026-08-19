## ADDED Requirements

### Requirement: Custom rules HTTP API

Micronaut runtime SHALL publish protected HTTP endpoints for custom rules management.

#### Scenario: Custom rules controller is routed

- **GIVEN** приложение запущено
- **WHEN** авторизованный клиент отправляет request to `/api/v1/rules`
- **THEN** Micronaut SHALL route request to custom rules controller

### Requirement: Custom rules configuration binding

Micronaut runtime SHALL bind custom rules limits from application configuration.

#### Scenario: Rule limits are configured

- **GIVEN** custom rules limits определены в application configuration
- **WHEN** приложение запускается
- **THEN** configured limits SHALL be available to custom rules registry
