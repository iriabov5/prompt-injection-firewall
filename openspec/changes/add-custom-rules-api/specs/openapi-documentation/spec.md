## ADDED Requirements

### Requirement: Custom rules API documentation

OpenAPI specification SHALL describe custom rules management endpoints.

#### Scenario: Custom rules operations are documented

- **GIVEN** custom rules endpoints реализованы
- **WHEN** OpenAPI specification генерируется
- **THEN** specification SHALL contain operations for creating, listing and deleting custom rules

#### Scenario: Custom rules schemas are documented

- **GIVEN** custom rules request and response DTO exist
- **WHEN** OpenAPI specification генерируется
- **THEN** specification SHALL describe schemas for custom rule creation and custom rule responses
