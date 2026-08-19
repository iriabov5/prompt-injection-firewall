# Спецификация openapi-documentation

## Purpose

Определяет требования к OpenAPI specification как машинно-читаемому контракту public HTTP endpoints, который генерируется из актуального Micronaut-кода и дополняет OpenSpec как поведенческий SSOT.

## Requirements

### Requirement: OpenAPI specification publication

Система SHALL публиковать или генерировать OpenAPI specification для public HTTP endpoints.

#### Scenario: OpenAPI specification доступна

- GIVEN приложение собрано или запущено
- WHEN клиент или build process запрашивает OpenAPI specification
- THEN система SHALL предоставить актуальную OpenAPI specification

### Requirement: Public endpoint coverage

OpenAPI specification SHALL описывать все public HTTP endpoints проекта.

#### Scenario: Public endpoint отражен в specification

- GIVEN public HTTP endpoint реализован в приложении
- WHEN OpenAPI specification генерируется
- THEN specification SHALL содержать operation для этого endpoint

### Requirement: Request and response schemas

OpenAPI specification SHALL описывать request и response schemas для public API.

#### Scenario: Prompt analysis schemas описаны

- GIVEN endpoint prompt analysis реализован
- WHEN OpenAPI specification генерируется
- THEN specification SHALL содержать schemas для request и response models этого endpoint

### Requirement: Error response documentation

OpenAPI specification SHALL описывать публичные error responses.

#### Scenario: Validation error описан

- GIVEN endpoint использует request validation
- WHEN OpenAPI specification генерируется
- THEN specification SHALL описывать validation error response

### Requirement: Swagger UI optionality

Система SHALL NOT требовать Swagger UI как обязательную часть OpenAPI capability.

#### Scenario: Swagger UI отсутствует

- GIVEN OpenAPI specification доступна
- WHEN Swagger UI не настроен
- THEN OpenAPI capability SHALL считаться реализованной

### Requirement: Swagger UI compatibility

OpenAPI specification SHALL быть пригодна для использования Swagger UI, оставаясь source of truth для browser-based API documentation.

#### Scenario: Specification используется Swagger UI

- GIVEN OpenAPI specification сгенерирована для public HTTP endpoints
- WHEN Swagger UI page загружается
- THEN Swagger UI SHALL читать эту specification без отдельного hand-written API contract

### Requirement: API key security scheme

OpenAPI specification SHALL describe the API key security scheme used by protected analysis endpoints.

#### Scenario: Security scheme is generated

- GIVEN OpenAPI specification генерируется
- WHEN specification описывает public API
- THEN specification SHALL include API key security scheme for header `X-API-Key`

#### Scenario: Protected operations reference security scheme

- GIVEN OpenAPI specification генерируется
- WHEN specification описывает `/api/v1/prompts/**` operations
- THEN protected operations SHALL reference API key security requirement

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
