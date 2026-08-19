## Implementation

- [x] Добавить custom rules configuration properties и application defaults для лимитов.
- [x] Добавить DTO/model для custom rules request, response, type и internal rule state.
- [x] Реализовать extension functions для normalization, matching и conversion to `RiskSignal`.
- [x] Реализовать lambda-with-receiver builder для custom rules и test fixtures.
- [x] Реализовать in-memory registry/service с validation, stable order и idempotent delete.
- [x] Реализовать `PromptRiskAnalyzer` для custom rules и подключить его к существующему pipeline.
- [x] Добавить protected Micronaut controller для `POST /api/v1/rules`, `GET /api/v1/rules`, `DELETE /api/v1/rules/{id}`.
- [x] Обновить OpenAPI annotations/test и README examples для custom rules API.
- [x] Добавить unit и integration tests согласно testing pyramid.
- [x] Запустить `openspec validate --all --strict --no-interactive`, `./gradlew test jacocoTestCoverageVerification` и SonarQube analysis.
