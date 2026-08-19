# micronaut-runtime Delta

## ADDED Requirements

### Requirement: HTTP API

Система SHALL публиковать операции анализа prompt через Micronaut HTTP controllers.

#### Scenario: Analyze endpoint доступен

- GIVEN приложение запущено
- WHEN клиент отправляет `POST /api/v1/prompts/analyze`
- THEN Micronaut SHALL направить запрос в controller

### Requirement: Request validation

Система SHALL валидировать incoming request bodies через Micronaut validation.

#### Scenario: Невалидный prompt отклоняется

- GIVEN request содержит blank prompt
- WHEN request отправляется
- THEN система SHALL вернуть validation error

### Requirement: Configuration binding

Система SHALL биндинговать firewall и AI settings через Micronaut configuration properties.

#### Scenario: Settings доступны компонентам

- GIVEN settings определены в application configuration
- WHEN приложение запускается
- THEN settings SHALL быть доступны соответствующим beans

### Requirement: Compile-time dependency injection

Система SHALL использовать Micronaut dependency injection для services и analyzers.

#### Scenario: Analyzers инжектятся коллекцией

- GIVEN существуют несколько `PromptRiskAnalyzer` beans
- WHEN создается `PromptFirewallService`
- THEN Micronaut SHALL inject analyzer collection

### Requirement: Micronaut HTTP client

Система SHALL использовать Micronaut HTTP client для AI provider calls.

#### Scenario: AI provider вызывается через Micronaut client

- GIVEN AI включен
- WHEN AI request отправляется
- THEN request SHALL использовать Micronaut HTTP client
