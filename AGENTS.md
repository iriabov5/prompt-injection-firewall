# Руководство по репозиторию

## Процесс разработки

Проект ведется через Specification Driven Development. Поведенческие требования живут в `openspec/specs/`, а каждое новое изменение оформляется как отдельный change в `openspec/changes/`.

Перед реализацией новой функциональности сначала создай или обнови OpenSpec change: `proposal.md`, delta specs, `design.md` и `tasks.md`. После реализации change архивируется, а delta specs переносятся в основные specs.

## Проверки перед коммитом

Перед каждым коммитом с изменениями кода или тестов обязательно выполни:

```bash
openspec validate --all --strict --no-interactive
./gradlew test jacocoTestCoverageVerification
./gradlew sonar -Dsonar.token=<token>
```

SonarQube должен быть запущен локально:

```bash
docker compose up -d sonarqube
```

Все замечания SonarQube по bugs, vulnerabilities, security hotspots и code smells надо исправить до коммита. Если замечание является false positive, причину нужно явно зафиксировать в change notes или commit message.

Если локальный SonarQube недоступен или token не создан, тесты и JaCoCo verification все равно обязательны, а в итоговом сообщении нужно явно указать, что SonarQube analysis не запускался.

## Код и тесты

Новые HTTP controllers для feature/API endpoints должны использовать Reactor boundary: возвращай `Mono<T>` или `Flux<T>`, даже если внутренняя операция быстрая и выполняется in-memory. Это сохраняет единый WebFlux-style API surface проекта. Синхронный DTO-ответ допустим только для простых diagnostic endpoints без бизнес-операций, например health.

Javadoc/KDoc для application code пишется на русском языке там, где он раскрывает назначение публичного контракта или нетривиальной логики. Не добавляй очевидные комментарии к точкам входа и простым DTO.

Тестовые классы и тестовые сценарии используют JUnit 5 `@DisplayName` на русском языке, чтобы отчет объяснял проверяемое поведение без чтения имен методов.
