# Bootstrap MVP

## Summary

Собрать первую версию Prompt Injection Firewall: сервис на Kotlin и Micronaut, который анализирует prompts на риск prompt injection через конкурентные эвристические анализаторы и опциональную AI-assisted классификацию.

## Motivation

LLM-приложениям нужен легкий safety boundary до того, как пользовательский ввод попадет в модель. MVP должен показать практичный AI security use case и одновременно раскрыть сильные стороны Micronaut: быстрый runtime, compile-time DI, configuration, conditional beans, HTTP clients, validation и test support.

## Scope

Входит в scope:

- Micronaut application skeleton;
- Gradle Kotlin DSL build;
- prompt analysis API;
- batch analysis API;
- heuristic analyzer pipeline;
- `CompletableFuture` orchestration;
- risk scoring and decisions;
- optional OpenAI-compatible AI analyzer;
- 80% JaCoCo coverage verification;
- testing pyramid.

Не входит в scope:

- persistent storage;
- frontend UI;
- user accounts;
- production deployment automation;
- provider-specific AI SDKs.

## Success Criteria

- Сервис запускается локально через Gradle.
- Анализ prompt работает без AI key.
- AI analyzer включается через configuration.
- Возможности Micronaut используются осознанно, а не случайно.
- Build проверяет tests и coverage.
- Public README остается кратким и ссылается на OpenSpec как SSOT.
