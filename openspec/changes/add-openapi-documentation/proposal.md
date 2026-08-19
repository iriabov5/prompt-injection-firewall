## Why

Публичные HTTP endpoints сервиса должны быть легко изучаемыми и проверяемыми без чтения исходного кода. OpenAPI specification позволит видеть контракты request/response models, error responses и доступные операции в стандартном формате.

## What Changes

- Добавляется требование публиковать OpenAPI specification для всех public HTTP endpoints.
- Добавляется требование описывать request и response schemas.
- Добавляется требование описывать validation и error responses.
- Добавляется требование держать OpenAPI specification синхронизированной с реализованными endpoints.
- Swagger UI в рамках этого change не является обязательным.

## Capabilities

### New Capabilities

- `openapi-documentation`: описание публикации OpenAPI specification, покрытия public endpoints и требований к схемам request/response/error.

### Modified Capabilities

- `micronaut-runtime`: runtime должен поддерживать публикацию OpenAPI specification через Micronaut-интеграцию.

## Impact

- Будущая реализация затронет Gradle dependencies и annotation processing.
- Будущая реализация затронет Micronaut configuration.
- Будущая реализация затронет controllers и DTO annotations, если это потребуется для корректной схемы.
- Поведение бизнес-анализа prompt не меняется.
