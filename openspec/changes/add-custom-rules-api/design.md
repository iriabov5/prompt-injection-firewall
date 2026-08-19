## Overview

Добавляем in-memory custom rules subsystem: protected HTTP API управляет правилами, registry хранит их в порядке создания, analyzer применяет enabled rules во время prompt analysis и возвращает `RiskSignal`.

## API

- `POST /api/v1/rules` — создать rule.
- `GET /api/v1/rules` — получить список rules.
- `DELETE /api/v1/rules/{id}` — удалить rule idempotently.

Rule types:

- `PHRASE`: case-insensitive contains по нормализованному prompt.
- `REGEX`: Kotlin Regex с предварительной validation на создании.

## Kotlin Features

Extension functions используются в domain layer:

- `String.normalizedForRuleMatching()`
- `CustomRule.matches(prompt: String)`
- `CustomRule.toRiskSignal()`

Lambda with receiver используется для небольшого rule builder, полезного и в production mapping, и в test fixtures:

- `customRule { ... }`
- `customRuleRequest { ... }` только если это упростит тесты без лишней абстракции.

Это не должно превращаться в DSL ради DSL: builder остается локальным и помогает убрать шум при создании правил.

## Micronaut Usage

- `@Controller` для HTTP endpoints.
- `@Singleton` registry/service для in-memory state.
- `@ConfigurationProperties` для limits.
- Bean analyzer внедряется в существующий `PromptFirewallService` через коллекцию `PromptRiskAnalyzer`.
- Existing API key security защищает `/api/v1/rules/**`.

## Validation

- DTO validation проверяет required fields, allowed weight range, length constraints и наличие нужного поля для выбранного rule type.
- Regex validation выполняется до сохранения rule.
- Rule count limit возвращает client error без изменения registry.

## Testing

- Unit tests: matching extensions, builder, registry limits, analyzer signals.
- Integration tests: create/list/delete endpoints, security without API key, custom rule affects prompt analysis.
- OpenAPI test: generated specification contains custom rules operations/schemas.

## Risks

- Regex rules могут быть дорогими. В этом этапе ограничиваем длину pattern и prompt уже существующим max prompt length; полноценная regex timeout strategy может стать отдельным change.
- In-memory storage подходит для pet/project demo и локального gateway, но не является durable state.
