## Commit Message

```text
Add API key security baseline
```

## 1. Security Setup

- [ ] 1.1 Добавить Micronaut Security dependencies.
- [ ] 1.2 Добавить configuration properties для `security.api-key`.
- [ ] 1.3 Настроить API key source через `PROMPT_FIREWALL_API_KEY` и test key в `application-test.yml`.
- [ ] 1.4 Настроить protected и anonymous route rules.

## 2. Authentication

- [ ] 2.1 Реализовать Micronaut Security authentication для header `X-API-Key`.
- [ ] 2.2 Отклонять requests без API key на `/api/v1/prompts/**`.
- [ ] 2.3 Отклонять requests с неверным API key на `/api/v1/prompts/**`.
- [ ] 2.4 Разрешать requests с валидным API key на `/api/v1/prompts/**`.
- [ ] 2.5 Гарантировать, что пустые configured keys не считаются валидными.
- [ ] 2.6 Гарантировать, что API key не попадает в logs и HTTP responses.

## 3. Runtime Security Policy

- [ ] 3.1 Оставить `/api/v1/health` доступным без API key.
- [ ] 3.2 Настроить access policy для `/swagger/**` и `/swagger-ui/**`.
- [ ] 3.3 Настроить access policy для `/metrics/**`.
- [ ] 3.4 Добавить baseline CORS configuration.
- [ ] 3.5 Добавить security headers для HTTP responses.

## 4. OpenAPI Documentation

- [ ] 4.1 Описать API key security scheme `X-API-Key` в generated OpenAPI.
- [ ] 4.2 Проверить, что protected analysis operations ссылаются на security requirement.
- [ ] 4.3 Проверить, что anonymous health operation не требует API key.

## 5. Tests

- [ ] 5.1 Добавить unit tests для API key validation.
- [ ] 5.2 Добавить integration tests: no key -> `401`.
- [ ] 5.3 Добавить integration tests: invalid key -> `401`.
- [ ] 5.4 Добавить integration tests: valid key -> `200`.
- [ ] 5.5 Добавить integration tests для anonymous health endpoint.
- [ ] 5.6 Добавить integration tests для docs/metrics access policy.
- [ ] 5.7 Добавить integration tests для CORS и security headers.
- [ ] 5.8 Проверить, что unauthorized response не раскрывает configured API key.

## 6. Documentation

- [ ] 6.1 Обновить README с `PROMPT_FIREWALL_API_KEY`.
- [ ] 6.2 Добавить curl-пример с header `X-API-Key`.
- [ ] 6.3 Пояснить, что API key создает operator/developer вне приложения.
- [ ] 6.4 Проверить, что новый production-код имеет полезный KDoc/Javadoc на русском, а новые тесты имеют русские `@DisplayName`.

## 7. Verification

- [ ] 7.1 Запустить `./gradlew test`.
- [ ] 7.2 Запустить `./gradlew jacocoTestCoverageVerification`.
- [ ] 7.3 Запустить `openspec validate --all --strict --no-interactive`.
