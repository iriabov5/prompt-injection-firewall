## Context

См. `proposal.md` для мотивации. Приложение сейчас имеет validation, health, metrics, OpenAPI и Swagger UI, но endpoints анализа prompt не требуют authentication.

## Goals / Non-Goals

**Goals:**

- Подключить Micronaut Security как штатный HTTP security layer.
- Защитить `/api/v1/prompts/**` API key authentication.
- Хранить API key только во внешней configuration/env, например `PROMPT_FIREWALL_API_KEY`.
- Оставить `/api/v1/health` anonymous.
- Явно настроить access rules для Swagger/OpenAPI/metrics endpoints.
- Описать API key security scheme в generated OpenAPI.
- Добавить baseline CORS и security headers.

**Non-Goals:**

- Не добавлять пользователей, роли, сессии, логины/пароли.
- Не добавлять JWT/OAuth2.
- Не хранить API keys в Postgres/Redis.
- Не делать UI для управления ключами.
- Не менять request/response contracts analysis endpoints.

## Decisions

1. Использовать Micronaut Security, а не самописный controller/filter-level guard.

   Альтернатива — написать собственный HTTP filter. Это проще в коротком коде, но хуже раскрывает Micronaut и легче ошибиться в ordering/routing.

2. Использовать API key в header `X-API-Key` как machine-to-machine authentication.

   Альтернатива — login/password через Basic Auth. Для API firewall без пользователей это слабее по модели: появляются учетные записи, которых в домене проекта нет.

3. API key задается оператором через environment/configuration и не генерируется приложением.

   Альтернатива — endpoint генерации ключей. Это потребовало бы storage, lifecycle, rotation и audit, что выходит за baseline.

4. Health endpoint остается public, а docs/metrics получают явные access rules.

   Альтернатива — закрыть всё одним правилом. Это ухудшает operational readiness: health обычно нужен orchestration/monitoring без business credential.

5. OpenAPI должен описывать `X-API-Key`, чтобы Swagger UI и внешние клиенты видели security contract.

   Альтернатива — оставить security только в README. Это создает расхождение между runtime behavior и API contract.

## Security Contract

| Route | Access |
| --- | --- |
| `/api/v1/prompts/**` | Requires valid `X-API-Key` |
| `/api/v1/health` | Anonymous |
| `/swagger/**` | Configured documentation policy |
| `/swagger-ui/**` | Configured documentation policy |
| `/metrics/**` | Configured management policy |

Configuration shape:

```yaml
security:
  api-key:
    enabled: true
    header-name: X-API-Key
    keys:
      - ${PROMPT_FIREWALL_API_KEY:}
```

Тестовый профиль может задавать `test-secret` через `application-test.yml`.

## Risks / Trade-offs

- [Risk] Пустой API key из env может случайно открыть protected endpoints -> Mitigation: при enabled security пустые keys не считаются валидными.
- [Risk] Swagger UI станет неудобно использовать после включения security -> Mitigation: OpenAPI описывает `X-API-Key`, а docs access policy задается configuration.
- [Risk] Security headers/CORS могут сломать browser clients -> Mitigation: CORS делается явным и покрывается integration tests.
- [Risk] Ключ попадет в logs или responses -> Mitigation: не логировать header value и тестировать отсутствие secret в unauthorized response.

## Migration Plan

1. Добавить Micronaut Security dependencies.
2. Добавить configuration properties для API key security.
3. Реализовать authentication через Micronaut Security extension points.
4. Настроить route access rules, CORS и security headers.
5. Обновить OpenAPI annotations/configuration.
6. Добавить unit и integration tests.
7. Обновить README.
8. Запустить tests, coverage verification и OpenSpec validation.
