## ADDED Requirements

### Requirement: Swagger UI compatibility

OpenAPI specification SHALL быть пригодна для использования Swagger UI, оставаясь source of truth для browser-based API documentation.

#### Scenario: Specification используется Swagger UI

- **GIVEN** OpenAPI specification сгенерирована для public HTTP endpoints
- **WHEN** Swagger UI page загружается
- **THEN** Swagger UI SHALL читать эту specification без отдельного hand-written API contract
