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

### Requirement: Metrics visibility

Micronaut runtime SHALL публиковать prompt analysis metrics через management endpoints без зависимости от включенного AI provider.

#### Scenario: Metrics доступны в heuristic mode

- GIVEN AI provider выключен
- WHEN developer запрашивает metrics management endpoint
- THEN Micronaut SHALL вернуть доступные runtime metrics
- AND core service availability SHALL NOT зависеть от external AI provider

### Requirement: Security filter pipeline

Micronaut runtime SHALL применять security rules к HTTP requests до выполнения protected controllers.

#### Scenario: Protected controller is not called without API key

- GIVEN API key security включена
- WHEN клиент отправляет request на protected analysis endpoint без валидного API key
- THEN Micronaut security pipeline SHALL reject request before prompt analysis starts

### Requirement: Configured anonymous routes

Micronaut runtime SHALL разрешать anonymous access только к явно configured public routes.

#### Scenario: Health remains anonymous

- GIVEN API key security включена
- WHEN клиент запрашивает health endpoint без API key
- THEN Micronaut SHALL allow request

#### Scenario: Metrics and docs routes are governed by configuration

- GIVEN management или documentation routes включены
- WHEN клиент запрашивает metrics, OpenAPI или Swagger UI route
- THEN Micronaut SHALL apply configured access rule for that route

### Requirement: Custom rules HTTP API

Micronaut runtime SHALL publish protected HTTP endpoints for custom rules management.

#### Scenario: Custom rules controller is routed

- **GIVEN** приложение запущено
- **WHEN** авторизованный клиент отправляет request to `/api/v1/rules`
- **THEN** Micronaut SHALL route request to custom rules controller

### Requirement: Custom rules configuration binding

Micronaut runtime SHALL bind custom rules limits from application configuration.

#### Scenario: Rule limits are configured

- **GIVEN** custom rules limits определены в application configuration
- **WHEN** приложение запускается
- **THEN** configured limits SHALL be available to custom rules registry

### Requirement: Reactive feature API boundary

Feature and business HTTP API controllers SHALL expose responses through Reactor types while lightweight diagnostic endpoints MAY return synchronous DTOs.

#### Scenario: Feature API uses reactive boundary

- **GIVEN** controller publishes feature or business API endpoint
- **WHEN** endpoint returns successful response
- **THEN** controller method SHALL expose response through Reactor `Mono` or `Flux`
- **AND** simple synchronous DTO responses SHALL be reserved for lightweight diagnostic endpoints

### Requirement: PostgreSQL persistence runtime

Micronaut runtime SHALL connect to PostgreSQL for audit persistence through configured datasource settings.

#### Scenario: Datasource is configured

- **GIVEN** PostgreSQL connection properties are configured
- **WHEN** приложение запускается
- **THEN** Micronaut SHALL create datasource and repository beans required for audit persistence

### Requirement: Database migrations

Micronaut runtime SHALL apply schema migrations for audit persistence.

#### Scenario: Audit table is created

- **GIVEN** PostgreSQL database is empty
- **WHEN** application starts with migrations enabled
- **THEN** migration SHALL create audit log table required by audit repository

### Requirement: Audit HTTP API

Micronaut runtime SHALL publish protected reactive HTTP endpoints for audit log queries.

#### Scenario: Audit controller is routed

- **GIVEN** приложение запущено
- **WHEN** авторизованный клиент отправляет request to `/api/v1/audit/events`
- **THEN** Micronaut SHALL route request to audit controller
- **AND** controller method SHALL expose response through Reactor `Mono` or `Flux`
