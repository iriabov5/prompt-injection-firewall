## Commit Message

```text
Add prompt analysis metrics
```

## 1. Metrics Setup

- [x] 1.1 Добавить Micronaut Micrometer dependency для runtime metrics.
- [x] 1.2 Включить management metrics endpoint в application configuration.
- [x] 1.3 Проверить, что health endpoint продолжает работать без изменений.

## 2. Instrumentation

- [x] 2.1 Добавить компонент записи prompt analysis metrics через `MeterRegistry`.
- [x] 2.2 Записывать counter для каждого analyzed prompt item.
- [x] 2.3 Добавить tags `decision`, `risk`, `source`, `ai_mode`, `ai_outcome` с ограниченным набором значений.
- [x] 2.4 Записывать latency timer для prompt analysis.
- [x] 2.5 Сохранить работу heuristic mode при выключенном AI provider.

## 3. Tests

- [x] 3.1 Добавить unit tests для записи counters и tags.
- [x] 3.2 Добавить unit tests для записи latency timer.
- [x] 3.3 Добавить integration test доступности management metrics endpoint.
- [x] 3.4 Проверить, что batch analysis учитывает каждый prompt item.
- [x] 3.5 Проверить имена metrics `prompt_firewall_analysis_total` и `prompt_firewall_analysis_latency`.

## 4. Documentation

- [x] 4.1 Обновить README с примером запроса metrics.
- [x] 4.2 Проверить, что новый production-код имеет полезный KDoc/Javadoc на русском, а новые тесты имеют русские `@DisplayName`.

## 5. Verification

- [x] 5.1 Запустить `./gradlew test`.
- [x] 5.2 Запустить `./gradlew jacocoTestCoverageVerification`.
- [x] 5.3 Запустить `openspec validate --all --strict --no-interactive`.
