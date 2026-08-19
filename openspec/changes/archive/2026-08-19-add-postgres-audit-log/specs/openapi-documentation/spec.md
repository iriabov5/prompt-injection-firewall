## ADDED Requirements

### Requirement: Audit API documentation

OpenAPI specification SHALL describe audit log endpoints and schemas.

#### Scenario: Audit operations are documented

- **GIVEN** audit endpoints реализованы
- **WHEN** OpenAPI specification генерируется
- **THEN** specification SHALL contain operations for latest audit events and audit statistics

#### Scenario: Audit schemas are documented

- **GIVEN** audit response DTO exist
- **WHEN** OpenAPI specification генерируется
- **THEN** specification SHALL describe schemas for audit event and audit statistics responses
