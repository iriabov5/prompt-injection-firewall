# Спецификация test-coverage

## Purpose

Определяет ожидания по тестированию и покрытию проекта.

## Requirements

### Requirement: Testing pyramid

Система SHALL тестироваться согласно testing pyramid.

#### Scenario: Unit tests покрывают core logic

- GIVEN существуют analyzer, aggregation и scoring logic
- WHEN пишутся tests
- THEN большинство tests SHALL быть unit tests
- AND unit tests SHALL NOT требовать полного Micronaut context

#### Scenario: Integration tests покрывают framework behavior

- GIVEN существуют Micronaut controllers, configuration, validation и conditional beans
- WHEN пишутся integration tests
- THEN они SHALL использовать Micronaut test support
- AND они SHALL проверять framework wiring и HTTP behavior

#### Scenario: API smoke tests покрывают main flows

- GIVEN существуют public API endpoints
- WHEN пишутся smoke tests
- THEN они SHALL покрывать safe prompt analysis
- AND они SHALL покрывать high-risk prompt analysis
- AND они SHALL покрывать batch analysis

### Requirement: Coverage threshold

Сборка SHALL требовать минимум 80% JaCoCo coverage для meaningful application logic.

#### Scenario: Coverage ниже threshold

- GIVEN application logic coverage ниже 80%
- WHEN verification запускается
- THEN build SHALL завершиться ошибкой

#### Scenario: Coverage соответствует threshold

- GIVEN application logic coverage не ниже 80%
- WHEN verification запускается
- THEN build SHALL успешно пройти

### Requirement: Coverage exclusions

Coverage rule SHALL allow excluding classes that do not contain meaningful behavior.

#### Scenario: Non-behavioral classes исключаются

- GIVEN class является application bootstrap, DTO-only, enum-only, generated code или configuration-property-only
- WHEN coverage рассчитывается
- THEN class SHALL be eligible for exclusion from coverage verification

### Requirement: Russian DisplayName for tests

Existing and new tests SHALL use Russian JUnit 5 `@DisplayName` annotations for test classes and test scenarios so that test reports explain what behavior is being verified.

#### Scenario: Existing tests are brought into compliance

- **WHEN** this change is implemented
- **THEN** already existing test classes SHALL have Russian `@DisplayName` annotations describing the tested component or behavior group
- **AND** already existing test methods SHALL have Russian `@DisplayName` annotations describing the specific verified scenario

#### Scenario: New tests are added later

- **WHEN** a future change adds a test class or test method
- **THEN** Russian `@DisplayName` annotations SHALL be added in the same change
- **AND** the display name SHALL explain the expected behavior rather than repeat the method name mechanically

#### Scenario: Test report is inspected

- **WHEN** developer opens the JUnit or Gradle test report
- **THEN** displayed test names SHALL be understandable in Russian without reading Kotlin method names
- **AND** displayed names SHALL make clear what is being tested and what outcome is expected
