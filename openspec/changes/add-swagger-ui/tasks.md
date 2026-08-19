## Commit Message

```text
Add Swagger UI documentation page
```

## 1. Swagger UI Setup

- [x] 1.1 Добавить Micronaut/Swagger UI dependency или static resources configuration.
- [x] 1.2 Настроить Swagger UI route для generated OpenAPI specification.
- [x] 1.3 Проверить, что OpenAPI YAML продолжает генерироваться стандартным способом.
- [x] 1.4 Обновить README с адресами OpenAPI YAML и Swagger UI.

## 2. Tests

- [x] 2.1 Добавить integration test доступности Swagger UI route.
- [x] 2.2 Добавить проверку, что Swagger UI ссылается на generated OpenAPI specification.
- [x] 2.3 Проверить, что prompt analysis endpoints не меняют behavior после добавления Swagger UI.

## 3. Verification

- [x] 3.1 Запустить `./gradlew test`.
- [x] 3.2 Запустить `./gradlew jacocoTestCoverageVerification`.
- [x] 3.3 Запустить `openspec validate --all --strict --no-interactive`.
