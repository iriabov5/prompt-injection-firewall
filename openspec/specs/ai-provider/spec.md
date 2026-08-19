# Спецификация ai-provider

## Purpose

Определяет опциональный AI-assisted анализ риска prompt через OpenAI-compatible provider.

## Requirements

### Requirement: AI опционален

Система SHALL работать без AI provider.

#### Scenario: AI выключен

- GIVEN `ai.enabled` равен `false`
- WHEN приложение запускается
- THEN AI analyzer bean SHALL NOT создаваться
- AND анализ prompt SHALL работать через эвристические анализаторы

### Requirement: Conditional AI analyzer

Система SHALL создавать AI analyzer только когда AI включен.

#### Scenario: AI включен

- GIVEN `ai.enabled` равен `true`
- WHEN приложение запускается
- THEN AI analyzer bean SHALL создаваться
- AND analyzer SHALL использовать настроенные параметры AI provider

### Requirement: OpenAI-compatible API

Система SHALL поддерживать OpenAI-compatible chat completions API.

#### Scenario: AI request отправляется

- GIVEN AI analysis включен
- AND валидный prompt анализируется
- WHEN AI analyzer запускается
- THEN он SHALL вызвать настроенный `base-url`
- AND он SHALL использовать настроенный `model`
- AND он SHALL аутентифицироваться через настроенный `api-key`

### Requirement: AI fallback

Система SHALL выдерживать сбои AI provider.

#### Scenario: AI request завершается по timeout

- GIVEN AI analysis включен
- AND AI provider не отвечает до истечения `ai.timeout-ms`
- WHEN prompt анализируется
- THEN система SHALL вернуть результаты эвристического анализа
- AND весь запрос SHALL NOT падать только из-за AI timeout

#### Scenario: AI возвращает некорректный response

- GIVEN AI analysis включен
- AND AI provider возвращает некорректный response
- WHEN prompt анализируется
- THEN система SHALL вернуть результаты эвристического анализа
- AND AI failure SHALL обрабатываться без раскрытия provider internals клиенту
