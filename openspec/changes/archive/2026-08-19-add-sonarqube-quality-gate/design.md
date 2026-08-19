## Overview

Изменение добавляет локальный SonarQube workflow как часть качества проекта. SonarQube будет запускаться отдельно через Docker Compose, а приложение останется обычным Micronaut service без runtime-зависимости от Sonar.

## Decisions

- Использовать официальный Docker image SonarQube Community Build для локального анализа.
- Не коммитить token и не задавать его в Gradle defaults.
- Добавить Gradle Sonar plugin и настроить `sonar.projectKey`, `sonar.projectName`, source/test paths и JaCoCo XML report path.
- Проверку качества документировать как pre-commit workflow: `test`, `jacocoTestCoverageVerification`, `sonar`.
- Не делать автоматический Git hook в рамках этого change: hooks локальны, не всегда переносятся через Git и могут мешать учебному workflow. Вместо этого команда будет явно описана в README и OpenSpec.

## Validation

- `openspec validate --all --strict --no-interactive`
- `./gradlew test`
- `./gradlew jacocoTestCoverageVerification`
- `./gradlew sonar -Dsonar.token=<token>` при запущенном SonarQube

## Risks

- Первый запуск SonarQube в Docker может занять несколько минут.
- Локальный SonarQube требует token из UI; token нельзя хранить в репозитории.
- Некоторые Sonar issues могут быть false positive. Их нельзя молча игнорировать: нужно исправить или явно описать причину исключения.
