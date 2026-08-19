# risk-scoring Delta

## ADDED Requirements

### Requirement: Normalized score

Система SHALL возвращать целочисленный score от `0` до `100`.

#### Scenario: Score нормализуется в допустимый диапазон

- GIVEN analyzer weights дают значение ниже `0` или выше `100`
- WHEN score нормализуется
- THEN score SHALL быть ограничен диапазоном `0..100`

### Requirement: Risk level mapping

Система SHALL сопоставлять score с risk level.

#### Scenario: Score превращается в risk level

- GIVEN нормализованный score рассчитан
- WHEN risk level определяется
- THEN система SHALL вернуть `LOW`, `MEDIUM` или `HIGH`

### Requirement: Decision mapping

Система SHALL сопоставлять risk level с decision.

#### Scenario: Risk level превращается в decision

- GIVEN risk level рассчитан
- WHEN decision определяется
- THEN система SHALL вернуть `ALLOW`, `REVIEW` или `BLOCK`
