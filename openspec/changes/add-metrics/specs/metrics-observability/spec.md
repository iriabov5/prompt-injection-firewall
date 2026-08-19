## Purpose

Определяет runtime-метрики анализа prompts, чтобы разработчик мог видеть нагрузку, решения firewall, latency и состояние AI-assisted части без чтения логов.

## ADDED Requirements

### Requirement: Prompt analysis request counter

Система SHALL считать количество выполненных prompt analysis requests.

#### Scenario: Single analysis increments counter

- **GIVEN** приложение запущено
- **WHEN** клиент выполняет single prompt analysis request
- **THEN** metrics SHALL содержать увеличенный счетчик prompt analysis requests

#### Scenario: Batch analysis increments counter per item

- **GIVEN** приложение запущено
- **WHEN** клиент выполняет batch prompt analysis request с несколькими items
- **THEN** metrics SHALL учитывать каждый проанализированный prompt item

### Requirement: Decision and risk tags

Prompt analysis metrics SHALL include tags for decision and risk level.

#### Scenario: Blocked prompt is tagged

- **GIVEN** prompt analysis вернул decision `BLOCK` и risk `HIGH`
- **WHEN** metrics публикуются
- **THEN** corresponding metric SHALL include decision `BLOCK` and risk `HIGH` tags

### Requirement: Analysis latency timer

Система SHALL измерять latency prompt analysis requests.

#### Scenario: Latency is recorded

- **GIVEN** prompt analysis request завершился
- **WHEN** metrics публикуются
- **THEN** metrics SHALL contain latency measurement for prompt analysis

### Requirement: AI outcome metrics

Система SHALL публиковать AI-assisted outcome metrics отдельно от core heuristic analysis.

#### Scenario: AI disabled metric visibility

- **GIVEN** AI provider выключен
- **WHEN** prompt analysis request завершился
- **THEN** metrics SHALL NOT требовать external AI call
- **AND** metrics SHALL отражать, что анализ выполнен в heuristic mode

#### Scenario: AI enabled outcome visibility

- **GIVEN** AI provider включен
- **WHEN** prompt analysis request завершился с AI-assisted result или AI failure
- **THEN** metrics SHALL отражать AI outcome отдельно от final firewall decision

### Requirement: Metrics endpoint publication

Система SHALL публиковать runtime metrics через management endpoint.

#### Scenario: Metrics endpoint доступен

- **GIVEN** приложение запущено
- **WHEN** developer запрашивает management metrics endpoint
- **THEN** система SHALL вернуть список или значение доступных metrics
