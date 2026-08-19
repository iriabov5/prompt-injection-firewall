# Спецификация micronaut-runtime

## Purpose

Определяет, как Micronaut используется как runtime приложения, а не только как библиотека роутинга.

## Requirements

### Requirement: HTTP API

Система SHALL публиковать операции анализа prompt через Micronaut HTTP controllers и SHALL поддерживать OpenAPI documentation для public HTTP endpoints.

#### Scenario: Analyze endpoint существует

- GIVEN приложение запущено
- WHEN клиент отправляет `POST /api/v1/prompts/analyze`
- THEN Micronaut SHALL направить запрос в prompt analysis controller

#### Scenario: OpenAPI documentation подключена к Micronaut runtime

- GIVEN public HTTP endpoints реализованы через Micronaut controllers
- WHEN OpenAPI specification генерируется
- THEN Micronaut OpenAPI integration SHALL использовать controller metadata для описания endpoints

### Requirement: Request validation

Система SHALL валидировать входящие request bodies через Micronaut validation.

#### Scenario: Пустой prompt отклоняется

- GIVEN request содержит пустой prompt
- WHEN request отправляется
- THEN система SHALL вернуть validation error
- AND анализ SHALL NOT запускаться

#### Scenario: Prompt сверх максимального размера отклоняется

- GIVEN prompt превышает настроенную максимальную длину
- WHEN request отправляется
- THEN система SHALL вернуть validation error
- AND анализ SHALL NOT запускаться

### Requirement: Configuration binding

Система SHALL биндинговать firewall и AI settings через Micronaut configuration properties.

#### Scenario: Firewall thresholds настроены

- GIVEN firewall thresholds определены в application configuration
- WHEN приложение запускается
- THEN настроенные thresholds SHALL быть доступны risk scoring components

### Requirement: Compile-time dependency injection

Система SHALL использовать Micronaut dependency injection для services и analyzers.

#### Scenario: Analyzer beans инжектятся

- GIVEN существует несколько `PromptRiskAnalyzer` beans
- WHEN создается `PromptFirewallService`
- THEN Micronaut SHALL внедрить коллекцию analyzers в service

### Requirement: Micronaut HTTP client

Система SHALL использовать Micronaut HTTP client для вызовов AI provider.

#### Scenario: AI client настроен

- GIVEN AI включен
- WHEN отправляется запрос к AI provider
- THEN request SHALL использовать Micronaut HTTP client configuration для base URL, headers, serialization и timeouts

### Requirement: Health visibility

Система SHALL публиковать runtime health information.

#### Scenario: Health в heuristic mode

- GIVEN AI выключен
- WHEN запрашивается health
- THEN service SHALL сообщить, что доступен для heuristic prompt analysis

#### Scenario: Health AI provider

- GIVEN AI включен
- WHEN запрашивается health
- THEN AI provider availability SHALL отображаться отдельно от core service availability

### Requirement: Swagger UI route

Micronaut runtime SHALL публиковать route или static resources для Swagger UI без влияния на core prompt analysis lifecycle.

#### Scenario: Swagger UI route не запускает анализ prompt

- GIVEN приложение запущено
- WHEN developer открывает Swagger UI route
- THEN Micronaut SHALL вернуть documentation UI
- AND prompt analysis SHALL NOT запускаться
