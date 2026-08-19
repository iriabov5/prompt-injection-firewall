## Why

Проекту нужен воспроизводимый способ проверять качество, security hotspots и покрытие не только через локальные тесты, но и через статический анализ. Это особенно важно для security-oriented сервиса, где перед коммитом нельзя полагаться только на компиляцию и ручной просмотр.

Цель: добавить локальный SonarQube workflow и закрепить правило, что перед коммитом разработчик запускает Sonar-анализ и исправляет найденные замечания.

Не-цели:

- не добавлять обязательную внешнюю SonarCloud-интеграцию;
- не хранить Sonar token в репозитории;
- не блокировать локальную разработку, если SonarQube явно не запущен.

## What Changes

- Добавляется локальная инфраструктура SonarQube через Docker Compose.
- Gradle получает настройку Sonar analysis с импортом JaCoCo XML coverage.
- README получает команды для запуска SonarQube, генерации token и анализа проекта.
- В OpenSpec фиксируется правило pre-commit quality check: перед коммитом надо запускать тесты, coverage verification и Sonar-анализ.
- Найденные SonarQube bugs, vulnerabilities, security hotspots и code smells должны исправляться перед коммитом либо явно документироваться как false positive с причиной.

## Capabilities

### New Capabilities

- `sonarqube-quality`: локальная проверка качества проекта через SonarQube, включая статический анализ, security review и импорт покрытия.

### Modified Capabilities

- `test-coverage`: процесс проверки перед коммитом расширяется обязательным quality-анализом SonarQube.

## Impact

- Gradle build configuration.
- Docker Compose configuration.
- README development instructions.
- OpenSpec specs and change tracking.
- Локальный developer workflow перед коммитом.
