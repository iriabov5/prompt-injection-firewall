## ADDED Requirements

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
