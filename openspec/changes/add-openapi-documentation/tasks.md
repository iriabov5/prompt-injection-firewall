## 1. OpenAPI specification setup

Commit message:

```text
Add OpenAPI documentation support
```

Tasks:

- [ ] Добавить Micronaut OpenAPI dependencies и annotation processing
- [ ] Настроить генерацию OpenAPI specification через Gradle/Micronaut
- [ ] Добавить metadata проекта для OpenAPI specification
- [ ] Убедиться, что specification публикуется или генерируется стандартным способом
- [ ] Описать request/response schemas для public endpoints
- [ ] Описать validation и error responses
- [ ] Добавить проверку генерации OpenAPI specification
- [ ] Проверить, что все public endpoints отражены в OpenAPI specification
- [ ] Запустить `./gradlew test`
- [ ] Запустить `openspec validate --all --strict --no-interactive`
