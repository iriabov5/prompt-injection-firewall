## MODIFIED Requirements

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
