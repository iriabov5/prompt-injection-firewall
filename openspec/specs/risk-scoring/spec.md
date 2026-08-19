# Спецификация risk-scoring

## Purpose

Определяет, как signals от анализаторов превращаются в нормализованный score, уровень риска и итоговое решение.

## Requirements

### Requirement: Нормализованный score

Система SHALL возвращать целочисленный score от `0` до `100`.

#### Scenario: Score ниже нуля нормализуется

- GIVEN веса анализаторов дают значение ниже `0`
- WHEN score нормализуется
- THEN score SHALL быть `0`

#### Scenario: Score выше ста нормализуется

- GIVEN веса анализаторов дают значение выше `100`
- WHEN score нормализуется
- THEN score SHALL быть `100`

### Requirement: Маппинг risk level

Система SHALL сопоставлять score с risk level.

#### Scenario: Низкий риск

- GIVEN нормализованный score находится в диапазоне от `0` до `29`
- WHEN risk level рассчитывается
- THEN risk SHALL быть `LOW`

#### Scenario: Средний риск

- GIVEN нормализованный score находится в диапазоне от `30` до `59`
- WHEN risk level рассчитывается
- THEN risk SHALL быть `MEDIUM`

#### Scenario: Высокий риск

- GIVEN нормализованный score находится в диапазоне от `60` до `100`
- WHEN risk level рассчитывается
- THEN risk SHALL быть `HIGH`

### Requirement: Маппинг decision

Система SHALL сопоставлять risk level с decision.

#### Scenario: Низкий риск разрешается

- GIVEN risk равен `LOW`
- WHEN decision рассчитывается
- THEN decision SHALL быть `ALLOW`

#### Scenario: Средний риск требует review

- GIVEN risk равен `MEDIUM`
- WHEN decision рассчитывается
- THEN decision SHALL быть `REVIEW`

#### Scenario: Высокий риск блокируется

- GIVEN risk равен `HIGH`
- WHEN decision рассчитывается
- THEN decision SHALL быть `BLOCK`

### Requirement: Объяснимые signals

Система SHALL возвращать machine-readable reasons и человекочитаемые описания signals.

#### Scenario: Signal участвует в verdict

- GIVEN анализатор формирует risk signal
- WHEN ответ строится
- THEN код signal SHALL быть доступен в `reasons`
- AND детали signal SHALL включать code, weight и description
