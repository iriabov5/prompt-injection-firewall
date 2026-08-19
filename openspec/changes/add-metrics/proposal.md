## Why

Сейчас сервис умеет анализировать prompts и отдавать health, но не показывает агрегированную runtime-картину: сколько запросов прошло, какие решения принимаются чаще и какова latency анализа. Метрики сделают проект полезнее как security layer и лучше раскроют Micronaut Management/metrics как часть runtime, а не как декоративную зависимость.

Цель изменения — добавить наблюдаемость для анализа prompt. Не-цели: добавлять постоянное хранилище, менять HTTP API анализа, подключать внешнюю observability platform или делать AI provider обязательным.

## What Changes

- Добавляется capability `metrics-observability` для runtime metrics prompt analysis.
- Система будет считать количество анализов по decision/risk/source.
- Система будет измерять latency анализа prompt.
- Система будет считать AI-assisted outcomes отдельно от core heuristic mode.
- Micronaut runtime будет публиковать metrics через management endpoint.
- README будет дополнен командой проверки metrics.

## Capabilities

### New Capabilities

- `metrics-observability`: runtime-метрики анализа prompts, доступные через Micronaut Management.

### Modified Capabilities

- `micronaut-runtime`: runtime visibility расширяется management metrics endpoint и не должна зависеть от включенного AI provider.

## Impact

- Micrometer/Micronaut metrics configuration.
- Instrumentation в сервисе анализа prompt или рядом с ним.
- Integration tests для management metrics endpoint.
- Unit tests для записи метрик по decisions, risks и latency.
- README с примером запроса metrics.
