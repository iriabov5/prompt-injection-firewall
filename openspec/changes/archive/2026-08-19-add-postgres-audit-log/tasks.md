## Implementation

- [x] Добавить Gradle dependencies для Micronaut Data JDBC, PostgreSQL, Flyway и Testcontainers PostgreSQL.
- [x] Расширить Docker Compose PostgreSQL service и application datasource/Flyway configuration.
- [x] Добавить Flyway migration для таблицы audit events.
- [x] Реализовать audit DTO/entity/repository и mapper без хранения original prompt.
- [x] Реализовать hashing/statistics service и best-effort audit writer после prompt analysis.
- [x] Добавить protected reactive audit controller для latest events и stats.
- [x] Обновить security configuration, OpenAPI annotations/test и README examples.
- [x] Добавить unit tests для hash/mapper/statistics.
- [x] Добавить Testcontainers PostgreSQL integration tests для migration/repository/controller/analysis audit recording.
- [x] Запустить `openspec validate --all --strict --no-interactive`, `./gradlew test jacocoTestCoverageVerification` и SonarQube analysis.
