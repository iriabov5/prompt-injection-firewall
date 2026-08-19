## Purpose

Определяет локальный quality workflow проекта через SonarQube, чтобы статический анализ, security review и покрытие проверялись воспроизводимо перед коммитом.

## ADDED Requirements

### Requirement: Local SonarQube runtime

Проект SHALL предоставлять локальный SonarQube runtime, который запускается через Docker Compose без изменения application runtime.

#### Scenario: Developer starts local quality server

- **WHEN** developer запускает локальную SonarQube-инфраструктуру
- **THEN** SonarQube SHALL быть доступен локально через documented HTTP URL
- **AND** запуск SonarQube SHALL NOT требовать изменения application configuration

### Requirement: Gradle Sonar analysis

Проект SHALL поддерживать Gradle-команду для отправки анализа в SonarQube с импортом JaCoCo XML coverage.

#### Scenario: Analysis includes coverage

- **GIVEN** tests and JaCoCo report выполнены
- **WHEN** developer запускает Sonar analysis
- **THEN** SonarQube SHALL receive Kotlin source analysis
- **AND** SonarQube SHALL import JaCoCo XML coverage report

### Requirement: Quality issues are resolved before commit

Developer workflow SHALL require resolving SonarQube issues before committing project changes.

#### Scenario: SonarQube reports issues

- **GIVEN** SonarQube analysis reports bugs, vulnerabilities, security hotspots or code smells
- **WHEN** developer prepares a commit
- **THEN** developer SHALL fix reported issues before commit
- **OR** developer SHALL document an intentional false positive with a clear reason

#### Scenario: SonarQube quality check passes

- **GIVEN** tests, coverage verification and SonarQube analysis complete successfully
- **WHEN** developer creates a commit
- **THEN** commit SHALL contain only changes that passed the documented quality workflow
