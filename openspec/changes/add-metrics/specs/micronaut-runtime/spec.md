## ADDED Requirements

### Requirement: Metrics visibility

Micronaut runtime SHALL публиковать prompt analysis metrics через management endpoints без зависимости от включенного AI provider.

#### Scenario: Metrics доступны в heuristic mode

- **GIVEN** AI provider выключен
- **WHEN** developer запрашивает metrics management endpoint
- **THEN** Micronaut SHALL вернуть доступные runtime metrics
- **AND** core service availability SHALL NOT зависеть от external AI provider
