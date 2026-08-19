## Purpose

Определяет пользовательские in-memory правила анализа prompt, чтобы приложение могло добавлять project-specific security checks без пересборки сервиса и без внешней базы данных.

## ADDED Requirements

### Requirement: Custom rule creation

Система SHALL позволять создавать пользовательские правила анализа prompt через protected HTTP API.

#### Scenario: Phrase rule is created

- **GIVEN** клиент авторизован валидным API key
- **WHEN** клиент создает enabled правило типа phrase с `code`, `phrase`, `weight` и `description`
- **THEN** система SHALL сохранить правило в памяти процесса
- **AND** система SHALL вернуть созданное правило с generated identifier

#### Scenario: Regex rule is created

- **GIVEN** клиент авторизован валидным API key
- **WHEN** клиент создает enabled правило типа regex с `code`, `pattern`, `weight` и `description`
- **THEN** система SHALL validate regex pattern
- **AND** система SHALL сохранить правило в памяти процесса

#### Scenario: Invalid rule is rejected

- **GIVEN** клиент авторизован валидным API key
- **WHEN** клиент отправляет правило без required fields, с invalid regex или weight вне allowed range
- **THEN** система SHALL вернуть validation error
- **AND** invalid rule SHALL NOT be stored

### Requirement: Custom rule listing

Система SHALL позволять получить список пользовательских правил через protected HTTP API.

#### Scenario: Rules are listed

- **GIVEN** custom rules существуют в памяти процесса
- **WHEN** авторизованный клиент запрашивает список правил
- **THEN** система SHALL вернуть rules в стабильном порядке создания

### Requirement: Custom rule deletion

Система SHALL позволять удалить пользовательское правило по identifier через protected HTTP API.

#### Scenario: Existing rule is deleted

- **GIVEN** custom rule существует
- **WHEN** авторизованный клиент удаляет rule по identifier
- **THEN** система SHALL удалить rule из памяти процесса
- **AND** последующий анализ prompt SHALL NOT учитывать удаленное rule

#### Scenario: Unknown rule delete is idempotent

- **GIVEN** rule с указанным identifier не существует
- **WHEN** авторизованный клиент удаляет rule по identifier
- **THEN** система SHALL return successful no-content response

### Requirement: In-memory rule limits

Система SHALL ограничивать количество custom rules и размер rule inputs через configuration.

#### Scenario: Rule count limit is reached

- **GIVEN** количество stored custom rules достигло configured limit
- **WHEN** клиент пытается создать новое rule
- **THEN** система SHALL reject creation with client error
- **AND** existing rules SHALL remain unchanged

### Requirement: Protected custom rules API

Custom rules API SHALL require the same API key security policy as prompt analysis endpoints.

#### Scenario: Missing API key is rejected

- **WHEN** клиент обращается к custom rules endpoint без API key
- **THEN** система SHALL reject request before custom rules state changes

### Requirement: Custom rule signal generation

Включенные custom rules SHALL produce risk signals during prompt analysis.

#### Scenario: Phrase rule matches prompt

- **GIVEN** enabled phrase rule содержит фразу, присутствующую в prompt
- **WHEN** prompt анализируется
- **THEN** response SHALL contain `RiskSignal` with custom rule code, weight and description

#### Scenario: Regex rule matches prompt

- **GIVEN** enabled regex rule matches prompt
- **WHEN** prompt анализируется
- **THEN** response SHALL contain `RiskSignal` with custom rule code, weight and description

#### Scenario: Disabled rule is ignored

- **GIVEN** disabled custom rule matches prompt text
- **WHEN** prompt анализируется
- **THEN** response SHALL NOT contain signal from that disabled rule
