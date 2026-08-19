## ADDED Requirements

### Requirement: PostgreSQL integration tests

Persistence behavior SHALL be verified with Testcontainers PostgreSQL integration tests.

#### Scenario: Audit repository uses real PostgreSQL

- **WHEN** audit persistence integration tests run
- **THEN** tests SHALL start PostgreSQL through Testcontainers
- **AND** tests SHALL verify schema migration and repository behavior against real PostgreSQL

#### Scenario: Persistence tests fit testing pyramid

- **WHEN** persistence feature is tested
- **THEN** unit tests SHALL cover pure hashing and statistics logic
- **AND** integration tests SHALL cover database wiring and HTTP behavior without replacing PostgreSQL with mocks
