## ADDED Requirements

### Requirement: PostgreSQL persistence runtime

Micronaut runtime SHALL connect to PostgreSQL for audit persistence through configured datasource settings.

#### Scenario: Datasource is configured

- **GIVEN** PostgreSQL connection properties are configured
- **WHEN** приложение запускается
- **THEN** Micronaut SHALL create datasource and repository beans required for audit persistence

### Requirement: Database migrations

Micronaut runtime SHALL apply schema migrations for audit persistence.

#### Scenario: Audit table is created

- **GIVEN** PostgreSQL database is empty
- **WHEN** application starts with migrations enabled
- **THEN** migration SHALL create audit log table required by audit repository

### Requirement: Audit HTTP API

Micronaut runtime SHALL publish protected reactive HTTP endpoints for audit log queries.

#### Scenario: Audit controller is routed

- **GIVEN** приложение запущено
- **WHEN** авторизованный клиент отправляет request to `/api/v1/audit/events`
- **THEN** Micronaut SHALL route request to audit controller
- **AND** controller method SHALL expose response through Reactor `Mono` or `Flux`
