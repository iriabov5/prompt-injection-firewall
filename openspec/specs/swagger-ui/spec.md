# Спецификация swagger-ui

## Purpose

Определяет интерактивную browser-based документацию public API, которая помогает разработчику быстро изучать endpoints и схемы без ручного чтения OpenAPI YAML.

## Requirements

### Requirement: Swagger UI publication

Система SHALL публиковать Swagger UI page для просмотра public HTTP API в браузере.

#### Scenario: Swagger UI доступен в браузере

- GIVEN приложение запущено
- WHEN developer открывает Swagger UI route
- THEN система SHALL вернуть Swagger UI page

### Requirement: Generated OpenAPI integration

Swagger UI SHALL отображать OpenAPI specification, сгенерированную из актуального Micronaut metadata.

#### Scenario: Swagger UI использует generated specification

- GIVEN OpenAPI specification доступна после сборки или запуска приложения
- WHEN Swagger UI page загружается
- THEN Swagger UI SHALL ссылаться на generated OpenAPI specification

### Requirement: API behavior isolation

Swagger UI SHALL NOT изменять behavior public API endpoints и SHALL NOT требовать включенного AI provider.

#### Scenario: Prompt analysis behavior не меняется

- GIVEN AI provider выключен
- WHEN Swagger UI доступен и клиент вызывает prompt analysis endpoint
- THEN prompt analysis SHALL работать в heuristic mode как до добавления Swagger UI
