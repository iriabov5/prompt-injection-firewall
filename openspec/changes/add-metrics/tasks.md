## Commit Message

```text
Add prompt analysis metrics
```

## 1. Metrics Setup

- [ ] 1.1 Добавить Micronaut Micrometer dependency для runtime metrics.
- [ ] 1.2 Включить management metrics endpoint в application configuration.
- [ ] 1.3 Проверить, что health endpoint продолжает работать без изменений.

## 2. Instrumentation

- [ ] 2.1 Добавить компонент записи prompt analysis metrics через `MeterRegistry`.
- [ ] 2.2 Записывать counter для каждого analyzed prompt item.
- [ ] 2.3 Добавить tags для `decision`, `risk`, `source` и AI mode/outcome с ограниченным набором значений.
- [ ] 2.4 Записывать latency timer для prompt analysis.
- [ ] 2.5 Сохранить работу heuristic mode при выключенном AI provider.

## 3. Tests

- [ ] 3.1 Добавить unit tests для записи counters и tags.
- [ ] 3.2 Добавить unit tests для записи latency timer.
- [ ] 3.3 Добавить integration test доступности management metrics endpoint.
- [ ] 3.4 Проверить, что batch analysis учитывает каждый prompt item.

## 4. Documentation

- [ ] 4.1 Обновить README с примером запроса metrics.
- [ ] 4.2 Проверить, что новый production-код имеет полезный KDoc/Javadoc на русском, а новые тесты имеют русские `@DisplayName`.

## 5. Verification

- [ ] 5.1 Запустить `./gradlew test`.
- [ ] 5.2 Запустить `./gradlew jacocoTestCoverageVerification`.
- [ ] 5.3 Запустить `openspec validate --all --strict --no-interactive`.
