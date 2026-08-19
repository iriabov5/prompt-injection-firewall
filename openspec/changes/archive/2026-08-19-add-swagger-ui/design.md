## Context

См. `proposal.md` для мотивации. В проекте уже есть OpenAPI generation через Micronaut metadata, а спецификация `openapi-documentation` явно говорит, что Swagger UI не является обязательной частью самой OpenAPI capability.

## Goals / Non-Goals

**Goals:**

- Подключить Swagger UI как тонкий browser-based слой поверх generated OpenAPI specification.
- Использовать Micronaut осознанно: OpenAPI annotation processing, static resources/routing и application configuration.
- Сохранить generated OpenAPI specification как контракт, который читает UI.
- Покрыть доступность UI и ссылку на generated specification тестами.

**Non-Goals:**

- Не менять существующие public API endpoints.
- Не писать OpenAPI YAML вручную.
- Не добавлять вызовы AI provider и не делать Swagger UI зависимым от AI configuration.
- Не превращать Swagger UI в отдельную бизнес-фичу firewall.

## Decisions

1. Swagger UI будет подключен через стандартную Micronaut OpenAPI/Swagger UI поддержку или минимальную static resources configuration, если версия Micronaut в проекте требует явного route.

   Альтернатива — положить собственную HTML-страницу вручную. Это хуже для проекта: придется сопровождать assets самим, а Micronaut уже умеет связывать generated OpenAPI artifact с документационным UI.

2. UI должен ссылаться на generated OpenAPI specification, а не на отдельный hand-written файл.

   Альтернатива — держать статический YAML только для Swagger UI. Это нарушило бы SSOT: поведение API описывается Micronaut metadata и OpenSpec, а UI должен читать производный OpenAPI artifact.

3. Проверки будут интеграционными: приложение поднимается Micronaut test runtime, после чего тест проверяет доступность Swagger UI route и наличие ссылки на OpenAPI specification.

   Альтернатива — тестировать только configuration properties. Этого мало, потому что пользовательская ценность изменения проявляется в HTTP route.

4. Новый код и тесты должны соблюдать существующую документационную спецификацию: KDoc/Javadoc на русском для production-кода там, где комментарий полезен, и русские `@DisplayName` для тестов.

## Risks / Trade-offs

- [Risk] Micronaut Swagger UI path может отличаться между версиями framework -> Mitigation: зафиксировать фактический route в configuration и README, а тестом проверять именно его.
- [Risk] UI может открываться, но смотреть не на актуальный OpenAPI artifact -> Mitigation: тестировать наличие ссылки на generated specification в HTML/configuration.
- [Risk] Добавление UI dependencies увеличит classpath -> Mitigation: использовать штатные Micronaut dependencies и не добавлять лишние webjars/assets вручную.

## Migration Plan

1. Добавить dependency/configuration для Swagger UI.
2. Проверить локально route Swagger UI и generated OpenAPI specification.
3. Обновить README.
4. Запустить тесты, coverage verification и OpenSpec validation.
