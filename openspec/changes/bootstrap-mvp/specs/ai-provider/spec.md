# ai-provider Delta

## ADDED Requirements

### Requirement: AI is optional

Система SHALL работать без AI provider.

#### Scenario: AI выключен

- GIVEN `ai.enabled` равен `false`
- WHEN приложение запускается
- THEN AI analyzer SHALL NOT создаваться
- AND prompt analysis SHALL работать через heuristic analyzers

### Requirement: OpenAI-compatible API

Система SHALL поддерживать OpenAI-compatible chat completions API.

#### Scenario: AI request использует настройки provider

- GIVEN AI analysis включен
- WHEN AI analyzer отправляет request
- THEN request SHALL использовать настроенные `base-url`, `api-key` и `model`

### Requirement: AI fallback

Система SHALL выдерживать сбои AI provider.

#### Scenario: AI failure не ломает анализ

- GIVEN AI provider вернул ошибку или timeout
- WHEN prompt анализируется
- THEN система SHALL вернуть результат на основе heuristic analyzers
