## ADDED Requirements

### Requirement: Pre-commit quality workflow

Project changes SHALL pass the documented local quality workflow before commit.

#### Scenario: Developer prepares code commit

- **WHEN** developer prepares a commit with code or test changes
- **THEN** developer SHALL run tests
- **AND** developer SHALL verify JaCoCo coverage threshold
- **AND** developer SHALL run SonarQube analysis when local SonarQube is available

#### Scenario: SonarQube is unavailable

- **GIVEN** local SonarQube is not running or token is unavailable
- **WHEN** developer prepares a commit
- **THEN** developer SHALL still run tests and JaCoCo coverage verification
- **AND** developer SHALL record that SonarQube analysis was not executed
