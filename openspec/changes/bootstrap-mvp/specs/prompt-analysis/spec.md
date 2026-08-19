# prompt-analysis Delta

## ADDED Requirements

### Requirement: Single prompt analysis

The system SHALL accept a single prompt analysis request and return a risk assessment.

#### Scenario: Safe prompt is allowed

- GIVEN a prompt does not contain suspicious instructions
- WHEN the prompt is analyzed
- THEN the system SHALL return risk `LOW`
- AND the system SHALL return decision `ALLOW`

#### Scenario: Instruction override prompt is blocked

- GIVEN a prompt asks the model to ignore previous instructions
- WHEN the prompt is analyzed
- THEN the system SHALL include reason `instruction_override`
- AND the system SHALL return risk `HIGH`
- AND the system SHALL return decision `BLOCK`

### Requirement: Concurrent analyzer execution

The system SHALL run independent analyzers concurrently with `CompletableFuture`.

#### Scenario: Analyzer futures are combined

- GIVEN multiple analyzers are available
- WHEN a prompt is analyzed
- THEN analyzer execution SHALL be represented as `CompletableFuture` operations
- AND the final response SHALL be produced after analyzer futures are combined
