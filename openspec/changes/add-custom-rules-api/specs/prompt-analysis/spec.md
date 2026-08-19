## ADDED Requirements

### Requirement: Custom rule analyzer participation

Система SHALL учитывать пользовательские custom rules как часть общего prompt analysis pipeline.

#### Scenario: Custom rules participate with built-in analyzers

- **GIVEN** существует enabled custom rule, которое matches prompt
- **WHEN** prompt анализируется
- **THEN** custom rule analyzer SHALL contribute risk signals
- **AND** final risk score SHALL include custom rule weights together with built-in analyzer signals

#### Scenario: Custom rules do not block other analyzers

- **GIVEN** custom rule analyzer returns no matching signals
- **WHEN** prompt анализируется
- **THEN** built-in heuristic analyzers SHALL still run normally
- **AND** final response SHALL remain valid
