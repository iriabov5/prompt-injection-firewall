# Спецификация api-key-security

## Purpose

Определяет базовую machine-to-machine защиту protected API endpoints через API key, который задается оператором сервиса через secret/configuration и не создается самим приложением.

## Requirements

### Requirement: API key protected endpoints

Система SHALL требовать валидный API key для доступа к protected endpoints, включая `/api/v1/prompts/**`, `/api/v1/rules/**` и `/api/v1/audit/**`.

#### Scenario: Request without API key is rejected

- **GIVEN** API key security включена
- **WHEN** клиент отправляет request на `/api/v1/prompts/analyze` без API key
- **THEN** система SHALL вернуть `401 Unauthorized`

#### Scenario: Request with invalid API key is rejected

- **GIVEN** API key security включена
- **WHEN** клиент отправляет request на `/api/v1/prompts/analyze` с неверным API key
- **THEN** система SHALL вернуть `401 Unauthorized`

#### Scenario: Request with valid API key is allowed

- **GIVEN** API key security включена
- **WHEN** клиент отправляет request на `/api/v1/prompts/analyze` с валидным API key
- **THEN** система SHALL выполнить prompt analysis

#### Scenario: Custom rules request without API key is rejected

- **GIVEN** API key security включена
- **WHEN** клиент отправляет request на `/api/v1/rules` без API key
- **THEN** система SHALL вернуть `401 Unauthorized`

#### Scenario: Audit request without API key is rejected

- **GIVEN** API key security включена
- **WHEN** клиент отправляет request на `/api/v1/audit/events` без API key
- **THEN** система SHALL вернуть `401 Unauthorized`

### Requirement: API key configuration

API keys SHALL задаваться через external configuration или environment secrets, а не храниться в source code.

#### Scenario: Operator provided key is accepted

- GIVEN operator задал API key через application configuration
- WHEN клиент передает тот же key в header `X-API-Key`
- THEN система SHALL аутентифицировать request

### Requirement: Public runtime endpoints

Система SHALL разрешать anonymous access к явно публичным runtime endpoints.

#### Scenario: Health endpoint remains public

- GIVEN API key security включена
- WHEN клиент запрашивает `/api/v1/health` без API key
- THEN система SHALL вернуть health response

#### Scenario: Documentation endpoints follow configured access policy

- GIVEN Swagger/OpenAPI endpoints включены configuration
- WHEN клиент запрашивает documentation endpoint
- THEN система SHALL применить configured documentation access policy

### Requirement: Secret safety

Система SHALL NOT логировать API key value и SHALL NOT возвращать его в HTTP responses.

#### Scenario: Unauthorized response does not expose secret

- GIVEN клиент отправляет неверный API key
- WHEN система возвращает `401 Unauthorized`
- THEN response SHALL NOT contain configured API key value

### Requirement: CORS and security headers baseline

Система SHALL применять baseline CORS policy и security headers для HTTP responses.

#### Scenario: CORS policy is explicit

- GIVEN CORS configuration задана
- WHEN browser client выполняет CORS preflight request
- THEN система SHALL применить configured CORS policy

#### Scenario: Security headers are present

- GIVEN приложение запущено
- WHEN клиент получает HTTP response
- THEN response SHALL include configured security headers
