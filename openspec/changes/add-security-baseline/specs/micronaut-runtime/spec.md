## ADDED Requirements

### Requirement: Security filter pipeline

Micronaut runtime SHALL применять security rules к HTTP requests до выполнения protected controllers.

#### Scenario: Protected controller is not called without API key

- **GIVEN** API key security включена
- **WHEN** клиент отправляет request на protected analysis endpoint без валидного API key
- **THEN** Micronaut security pipeline SHALL reject request before prompt analysis starts

### Requirement: Configured anonymous routes

Micronaut runtime SHALL разрешать anonymous access только к явно configured public routes.

#### Scenario: Health remains anonymous

- **GIVEN** API key security включена
- **WHEN** клиент запрашивает health endpoint без API key
- **THEN** Micronaut SHALL allow request

#### Scenario: Metrics and docs routes are governed by configuration

- **GIVEN** management или documentation routes включены
- **WHEN** клиент запрашивает metrics, OpenAPI или Swagger UI route
- **THEN** Micronaut SHALL apply configured access rule for that route
