# audit-log Specification

## Purpose
Определяет persistent security audit trail результатов анализа prompt в PostgreSQL, чтобы решения firewall можно было расследовать после рестарта без сохранения исходного prompt.

## Requirements

### Requirement: Audit event persistence

Система SHALL сохранять результат успешного prompt analysis как audit event в PostgreSQL.

#### Scenario: Single analysis is stored

- **GIVEN** анализ одного prompt завершился успешно
- **WHEN** итоговый response сформирован
- **THEN** система SHALL сохранить audit event в PostgreSQL
- **AND** audit event SHALL contain score, risk, decision, reasons, latency and created timestamp

#### Scenario: Batch analysis stores every item

- **GIVEN** batch request contains multiple prompts
- **WHEN** batch analysis completes successfully
- **THEN** система SHALL сохранить отдельный audit event для каждого prompt item
- **AND** stored events SHALL preserve individual analysis decisions

### Requirement: Prompt privacy

Audit log SHALL NOT store original prompt text.

#### Scenario: Prompt hash is stored instead of prompt

- **GIVEN** prompt analysis creates audit event
- **WHEN** event is persisted
- **THEN** audit event SHALL contain deterministic prompt hash
- **AND** audit event SHALL NOT contain original prompt text

### Requirement: Audit event query

Система SHALL предоставлять protected API для просмотра последних audit events.

#### Scenario: Latest events are returned

- **GIVEN** audit events exist in PostgreSQL
- **WHEN** авторизованный клиент запрашивает latest audit events
- **THEN** система SHALL return events ordered from newest to oldest
- **AND** response SHALL respect configured maximum page size

### Requirement: Audit statistics

Система SHALL предоставлять protected API с aggregate statistics по audit decisions.

#### Scenario: Decision statistics are returned

- **GIVEN** audit events exist in PostgreSQL
- **WHEN** авторизованный клиент запрашивает audit statistics
- **THEN** response SHALL contain counts grouped by decision
- **AND** response SHALL contain total event count

### Requirement: Protected audit API

Audit API SHALL require API key authentication.

#### Scenario: Missing API key is rejected

- **WHEN** клиент обращается к audit endpoint без API key
- **THEN** система SHALL return `401 Unauthorized`
- **AND** audit data SHALL NOT be returned

### Requirement: Persistence failure tolerance

Audit persistence failures SHALL NOT break prompt analysis response.

#### Scenario: Audit storage fails

- **GIVEN** prompt analysis completed successfully
- **AND** audit storage is temporarily unavailable
- **WHEN** system tries to persist audit event
- **THEN** prompt analysis response SHALL still be returned to the client
- **AND** failure SHALL be logged without original prompt text
