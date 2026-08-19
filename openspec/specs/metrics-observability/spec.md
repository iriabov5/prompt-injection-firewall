# Спецификация metrics-observability

## Purpose

Определяет runtime-метрики анализа prompts, чтобы разработчик мог видеть нагрузку, решения firewall, latency и состояние AI-assisted части без чтения логов.

## Requirements

### Requirement: Prompt analysis request counter

Система SHALL считать количество выполненных prompt analysis requests через metric `prompt_firewall_analysis_total`.

#### Scenario: Single analysis increments counter

- GIVEN приложение запущено
- WHEN клиент выполняет single prompt analysis request
- THEN metrics SHALL содержать увеличенный счетчик `prompt_firewall_analysis_total`

#### Scenario: Batch analysis increments counter per item

- GIVEN приложение запущено
- WHEN клиент выполняет batch prompt analysis request с несколькими items
- THEN metric `prompt_firewall_analysis_total` SHALL учитывать каждый проанализированный prompt item

### Requirement: Decision and risk tags

Prompt analysis metrics SHALL include bounded tags for `decision`, `risk`, `source`, `ai_mode` and `ai_outcome`.

#### Scenario: Blocked prompt is tagged

- GIVEN prompt analysis вернул decision `BLOCK` и risk `HIGH`
- WHEN metrics публикуются
- THEN metric `prompt_firewall_analysis_total` SHALL include decision `BLOCK` and risk `HIGH` tags

#### Scenario: Source tag is normalized

- GIVEN prompt analysis request не содержит source
- WHEN metrics публикуются
- THEN metric `prompt_firewall_analysis_total` SHALL include source `unknown`

### Requirement: Analysis latency timer

Система SHALL измерять latency prompt analysis requests через metric `prompt_firewall_analysis_latency`.

#### Scenario: Latency is recorded

- GIVEN prompt analysis request завершился
- WHEN metrics публикуются
- THEN metrics SHALL contain timer `prompt_firewall_analysis_latency`

### Requirement: AI outcome metrics

Система SHALL публиковать AI-assisted outcome через tags `ai_mode` и `ai_outcome` в prompt analysis metrics.

#### Scenario: AI disabled metric visibility

- GIVEN AI provider выключен
- WHEN prompt analysis request завершился
- THEN metrics SHALL NOT требовать external AI call
- AND metric `prompt_firewall_analysis_total` SHALL include ai_mode `disabled` and ai_outcome `skipped`

#### Scenario: AI enabled outcome visibility

- GIVEN AI provider включен
- WHEN prompt analysis request завершился с AI-assisted result или AI failure
- THEN metric `prompt_firewall_analysis_total` SHALL include ai_mode `enabled`
- AND metric SHALL include ai_outcome `success` или `failed`

### Requirement: Metrics endpoint publication

Система SHALL публиковать runtime metrics через management endpoint.

#### Scenario: Metrics endpoint доступен

- GIVEN приложение запущено
- WHEN developer запрашивает management metrics endpoint
- THEN система SHALL вернуть список или значение доступных metrics
