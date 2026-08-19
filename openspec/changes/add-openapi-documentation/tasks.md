## 1. OpenAPI specification setup

Commit message:

```text
Add OpenAPI documentation support
```

Tasks:

- [x] Добавить Micronaut OpenAPI dependencies и annotation processing
- [x] Настроить генерацию OpenAPI specification через Gradle/Micronaut
- [x] Добавить metadata проекта для OpenAPI specification
- [x] Убедиться, что specification публикуется или генерируется стандартным способом
- [x] Описать request/response schemas для public endpoints
- [x] Описать validation и error responses
- [x] Добавить проверку генерации OpenAPI specification
- [x] Проверить, что все public endpoints отражены в OpenAPI specification
- [x] Запустить `./gradlew test`
- [x] Запустить `openspec validate --all --strict --no-interactive`
