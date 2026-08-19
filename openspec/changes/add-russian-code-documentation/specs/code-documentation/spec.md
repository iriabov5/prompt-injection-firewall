## Purpose

Определяет требования к русскоязычной документации production-кода, чтобы публичный репозиторий был понятен при чтении в IDE, code review и сопровождении без обращения к истории обсуждений.

## ADDED Requirements

### Requirement: Russian KDoc for production code
Existing and new production Kotlin code SHALL contain Russian KDoc/Javadoc for public and internal project declarations whose purpose, contract, constraints, concurrency behavior, configuration role, HTTP role or security meaning are not fully obvious from the declaration name alone.

#### Scenario: Reader inspects production code in IDE
- **WHEN** developer opens a production class, interface, enum, data model, controller, configuration component, service or analyzer
- **THEN** meaningful declarations SHALL have Russian KDoc/Javadoc explaining their role in the prompt firewall
- **AND** documentation SHALL clarify important input, output, decision, timeout, concurrency or security semantics when they affect correct usage

#### Scenario: Existing production code is brought into compliance
- **WHEN** this change is implemented
- **THEN** already existing production Kotlin files SHALL be reviewed for missing Russian KDoc/Javadoc
- **AND** missing meaningful documentation SHALL be added before the change is considered complete

#### Scenario: New production code is added later
- **WHEN** a future change adds a production class, interface, enum, data model, controller, configuration component, service or analyzer
- **THEN** Russian KDoc/Javadoc SHALL be added in the same change where the new declaration appears
- **AND** the new code SHALL NOT rely on a later documentation pass to become understandable

#### Scenario: Obvious code does not receive noisy comments
- **WHEN** declaration meaning is already fully clear from its name and type
- **THEN** documentation SHALL NOT duplicate obvious implementation details
- **AND** documentation SHALL remain concise enough to help reading rather than hide the code

### Requirement: Documentation stays aligned with behavior
Russian KDoc/Javadoc SHALL be updated together with behavior changes that alter public contracts, risk decisions, validation rules, configuration semantics or analyzer meaning.

#### Scenario: Behavior changes after documentation exists
- **WHEN** implementation changes a documented contract, rule or runtime behavior
- **THEN** corresponding Russian KDoc/Javadoc SHALL be updated in the same change
- **AND** stale documentation SHALL NOT remain in the codebase
