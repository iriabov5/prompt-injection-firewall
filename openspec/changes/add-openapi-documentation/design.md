## Overview

OpenAPI documentation должна быть добавлена как runtime/documentation capability поверх существующего Micronaut-приложения.

Основной источник контрактов остается в коде controllers и DTO, а OpenAPI specification должна генерироваться стандартными средствами Micronaut OpenAPI.

## Micronaut Integration

Реализация должна использовать Micronaut OpenAPI integration и annotation processing, чтобы specification генерировалась из актуального application code.

Не нужно писать OpenAPI YAML вручную как отдельный SSOT. SSOT по поведению остается в `openspec/specs/`, а OpenAPI specification является машинно-читаемым контрактом, полученным из реализации.

## Endpoint Coverage

OpenAPI specification должна покрывать все public HTTP endpoints, включая:

- single prompt analysis;
- batch prompt analysis;
- rules endpoint, если он реализован;
- health или management endpoints, если они считаются public API проекта.

## Schemas

Specification должна описывать:

- request bodies;
- response bodies;
- enum values;
- validation constraints;
- standard validation errors;
- unexpected error response, если он публично документируется.

## Swagger UI

Swagger UI не обязателен для первого implementation pass. Его можно добавить отдельным change, если понадобится интерактивная документация.

## Testing

Минимальная проверка должна подтверждать, что OpenAPI artifact генерируется и содержит public endpoints. Если endpoint генерации доступен только после запуска приложения, проверка может быть integration-level.
