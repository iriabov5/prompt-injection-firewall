## Why

Сервис уже анализирует prompts и публикует runtime-документацию, но analysis endpoints пока открыты для любого клиента. Security baseline нужен, чтобы проект выглядел как реальный machine-to-machine firewall layer: доступ к анализу защищен API key, а runtime endpoints остаются явно описанными.

Цель изменения — добавить базовую защиту HTTP API через Micronaut Security. Не-цели: вводить пользователей, логины/пароли, сессии, JWT/OAuth2, БД для ключей или полноценный key management.

## What Changes

- Добавляется API key authentication для `/api/v1/prompts/**`.
- API key создается вне приложения оператором/разработчиком и передается через secret/env configuration.
- Клиент передает ключ в header `X-API-Key`.
- Без ключа или с неверным ключом protected endpoints возвращают `401 Unauthorized`.
- `/api/v1/health` остается доступным без API key.
- Swagger/OpenAPI и metrics endpoints получают явно описанные access rules через configuration.
- OpenAPI specification описывает API key security scheme для protected analysis endpoints.
- Security headers и CORS policy фиксируются как baseline runtime behavior.

## Capabilities

### New Capabilities

- `api-key-security`: machine-to-machine API key authentication для защищенных analysis endpoints.

### Modified Capabilities

- `micronaut-runtime`: runtime получает Micronaut Security filter/rules, CORS и security headers как часть HTTP pipeline.
- `openapi-documentation`: OpenAPI specification должна описывать API key security scheme и protected operations.

## Impact

- Micronaut Security dependencies и configuration.
- Новый security configuration properties bean для API key settings.
- Authentication/token validation logic через Micronaut Security.
- OpenAPI annotations/configuration для `X-API-Key`.
- Integration tests на `401/200`, anonymous health/docs/metrics behavior, CORS/security headers.
- README с примером запуска через `PROMPT_FIREWALL_API_KEY` и запроса с `X-API-Key`.
