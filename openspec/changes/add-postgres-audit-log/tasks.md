## Implementation

- [ ] Добавить Gradle dependencies для Micronaut Data JDBC, PostgreSQL, Flyway и Testcontainers PostgreSQL.
- [ ] Расширить Docker Compose PostgreSQL service и application datasource/Flyway configuration.
- [ ] Добавить Flyway migration для таблицы audit events.
- [ ] Реализовать audit DTO/entity/repository и mapper без хранения original prompt.
- [ ] Реализовать hashing/statistics service и best-effort audit writer после prompt analysis.
- [ ] Добавить protected reactive audit controller для latest events и stats.
- [ ] Обновить security configuration, OpenAPI annotations/test и README examples.
- [ ] Добавить unit tests для hash/mapper/statistics.
- [ ] Добавить Testcontainers PostgreSQL integration tests для migration/repository/controller/analysis audit recording.
- [ ] Запустить `openspec validate --all --strict --no-interactive`, `./gradlew test jacocoTestCoverageVerification` и SonarQube analysis.
