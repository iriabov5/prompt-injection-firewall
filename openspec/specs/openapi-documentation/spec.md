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
