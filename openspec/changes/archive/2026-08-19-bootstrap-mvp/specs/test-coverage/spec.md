# test-coverage Delta

## ADDED Requirements

### Requirement: Testing pyramid

Система SHALL тестироваться согласно testing pyramid.

#### Scenario: Tests распределены по пирамиде

- GIVEN project tests реализованы
- WHEN test suite просматривается
- THEN большинство tests SHALL быть unit tests
- AND integration и smoke tests SHALL покрывать framework и API boundaries

### Requirement: Coverage threshold

Build SHALL требовать минимум 80% JaCoCo coverage для meaningful application logic.

#### Scenario: Coverage verification включен

- GIVEN coverage ниже 80%
- WHEN build verification запускается
- THEN build SHALL fail
