## ADDED Requirements

### Requirement: Prompt analysis audit recording

Система SHALL record audit metadata after successful prompt analysis without changing the analysis response contract.

#### Scenario: Analysis response is returned while audit is recorded

- **GIVEN** prompt analysis completes successfully
- **WHEN** audit recording is enabled
- **THEN** client SHALL receive the same prompt analysis response shape
- **AND** audit recording SHALL happen after the final decision is available

#### Scenario: Audit recording does not affect analyzer execution

- **GIVEN** audit storage is configured
- **WHEN** prompt is analyzed
- **THEN** analyzers SHALL still execute through the existing `CompletableFuture` pipeline
- **AND** audit recording SHALL NOT become a prompt risk analyzer
