# Bootstrap MVP Tasks

## Правила этапной разработки

Каждый этап выполняется отдельным коммитом. В коммит попадают только файлы, относящиеся к задачам этапа.

Перед каждым коммитом SHALL выполняться проверки этапа. Если проверка недоступна из-за еще не созданного кода или инфраструктуры, это SHALL быть явно указано в итоговом сообщении этапа.

После выполнения задач этапа чекбоксы SHALL обновляться с `[ ]` на `[x]`.

OpenSpec SHALL оставаться валидным после каждого этапа:

```bash
openspec validate --all --strict --no-interactive
```

## Этап 1. Project setup

Цель: создать минимальный запускаемый Micronaut-проект с Gradle Kotlin DSL и базовой тестовой инфраструктурой.

Commit message:

```text
Bootstrap Gradle Micronaut project
```

Проверки перед коммитом:

```bash
./gradlew test
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Создать Gradle Kotlin DSL project
- [x] Добавить Micronaut Kotlin dependencies
- [x] Добавить Reactor support
- [x] Добавить JUnit 5, MockK и JaCoCo
- [x] Настроить JaCoCo coverage report
- [x] Настроить JaCoCo coverage verification на 80%
- [x] Добавить минимальный application config
- [x] Добавить минимальный smoke test запуска context

## Этап 2. Domain model

Цель: описать модели prompt analysis без привязки к Micronaut runtime.

Commit message:

```text
Add prompt analysis domain model
```

Проверки перед коммитом:

```bash
./gradlew test
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Добавить request model для prompt analysis
- [x] Добавить response model для prompt analysis
- [x] Добавить batch request и batch response models
- [x] Добавить risk signal model
- [x] Добавить risk level enum
- [x] Добавить decision enum
- [x] Добавить unit tests для model constraints, если появится behavior

## Этап 3. Risk scoring

Цель: реализовать объяснимое преобразование signals в score, risk level и decision.

Commit message:

```text
Add explainable risk scoring
```

Проверки перед коммитом:

```bash
./gradlew test
./gradlew jacocoTestCoverageVerification
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Добавить score normalizer
- [x] Добавить risk aggregator
- [x] Замаппить score в risk level
- [x] Замаппить risk level в decision
- [x] Добавить reasons из signals
- [x] Добавить unit tests для score boundaries
- [x] Добавить unit tests для risk level mapping
- [x] Добавить unit tests для decision mapping
- [x] Добавить unit tests для explainable signals

## Этап 4. Analyzer pipeline

Цель: реализовать эвристические анализаторы и конкурентную оркестрацию через `CompletableFuture`.

Commit message:

```text
Add concurrent prompt analyzer pipeline
```

Проверки перед коммитом:

```bash
./gradlew test
./gradlew jacocoTestCoverageVerification
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Добавить interface `PromptRiskAnalyzer`
- [x] Добавить jailbreak phrase analyzer
- [x] Добавить system prompt leak analyzer
- [x] Добавить encoding obfuscation analyzer
- [x] Добавить URL instruction analyzer
- [x] Добавить markdown injection analyzer
- [x] Добавить concurrent analyzer orchestration через `CompletableFuture`
- [x] Добавить timeout для analyzer execution
- [x] Добавить fallback при ошибке одного analyzer
- [x] Добавить unit tests для каждого analyzer
- [x] Добавить unit tests для orchestration и fault tolerance

## Этап 5. Micronaut runtime

Цель: раскрыть Micronaut как runtime shell: HTTP, DI, validation, configuration и health.

Commit message:

```text
Add Micronaut runtime integration
```

Проверки перед коммитом:

```bash
./gradlew test
./gradlew jacocoTestCoverageVerification
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Добавить prompt analysis controller
- [x] Добавить batch analysis controller method
- [x] Добавить validation annotations
- [x] Добавить firewall configuration properties
- [x] Добавить health endpoint или health indicator
- [x] Подключить analyzer beans через Micronaut DI
- [x] Вернуть Reactor type на HTTP boundary
- [x] Добавить Micronaut integration tests для controller behavior
- [x] Добавить Micronaut integration tests для validation errors
- [x] Добавить Micronaut integration tests для configuration binding
- [x] Добавить API smoke tests для safe и high-risk prompts

## Этап 6. AI provider

Цель: добавить опциональный OpenAI-compatible AI analyzer без обязательной зависимости от внешнего API.

Commit message:

```text
Add optional OpenAI compatible analyzer
```

Проверки перед коммитом:

```bash
./gradlew test
./gradlew jacocoTestCoverageVerification
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Добавить interface `AiClient`
- [x] Добавить AI configuration properties
- [x] Добавить OpenAI-compatible HTTP client через Micronaut HTTP client
- [x] Добавить conditional AI analyzer bean через `ai.enabled`
- [x] Добавить timeout и fallback behavior
- [x] Добавить fake AI client для tests
- [x] Добавить unit tests для AI analyzer
- [x] Добавить Micronaut integration tests для AI disabled mode
- [x] Добавить Micronaut integration tests для conditional AI bean

## Этап 7. MVP hardening

Цель: довести MVP до состояния, где build, specs и README согласованы.

Commit message:

```text
Finalize MVP validation workflow
```

Проверки перед коммитом:

```bash
./gradlew check
openspec validate --all --strict --no-interactive
```

Tasks:

- [x] Проверить `./gradlew check`
- [x] Проверить 80% coverage
- [x] Проверить OpenSpec strict validation
- [x] Обновить README, если команды запуска или API изменились
- [x] Проверить, что `openspec/specs/` соответствует реализованному behavior
- [x] Подготовить итоговый список реализованных возможностей

## Этап 8. Archive bootstrap change

Цель: закрыть `bootstrap-mvp` change после готовности MVP.

Commit message:

```text
Archive bootstrap MVP OpenSpec change
```

Проверки перед коммитом:

```bash
openspec archive bootstrap-mvp --yes
openspec validate --all --strict --no-interactive
```

Tasks:

- [ ] Убедиться, что все tasks в `bootstrap-mvp` выполнены
- [ ] Выполнить archive change
- [ ] Проверить, что SSOT specs обновлены
- [ ] Закоммитить archive result
