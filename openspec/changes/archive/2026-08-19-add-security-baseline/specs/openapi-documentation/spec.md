## ADDED Requirements

### Requirement: API key security scheme

OpenAPI specification SHALL describe the API key security scheme used by protected analysis endpoints.

#### Scenario: Security scheme is generated

- **GIVEN** OpenAPI specification генерируется
- **WHEN** specification описывает public API
- **THEN** specification SHALL include API key security scheme for header `X-API-Key`

#### Scenario: Protected operations reference security scheme

- **GIVEN** OpenAPI specification генерируется
- **WHEN** specification описывает `/api/v1/prompts/**` operations
- **THEN** protected operations SHALL reference API key security requirement
