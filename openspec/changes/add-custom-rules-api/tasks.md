## Implementation

- [ ] Добавить custom rules configuration properties и application defaults для лимитов.
- [ ] Добавить DTO/model для custom rules request, response, type и internal rule state.
- [ ] Реализовать extension functions для normalization, matching и conversion to `RiskSignal`.
- [ ] Реализовать lambda-with-receiver builder для custom rules и test fixtures.
- [ ] Реализовать in-memory registry/service с validation, stable order и idempotent delete.
- [ ] Реализовать `PromptRiskAnalyzer` для custom rules и подключить его к существующему pipeline.
- [ ] Добавить protected Micronaut controller для `POST /api/v1/rules`, `GET /api/v1/rules`, `DELETE /api/v1/rules/{id}`.
- [ ] Обновить OpenAPI annotations/test и README examples для custom rules API.
- [ ] Добавить unit и integration tests согласно testing pyramid.
- [ ] Запустить `openspec validate --all --strict --no-interactive`, `./gradlew test jacocoTestCoverageVerification` и SonarQube analysis.
