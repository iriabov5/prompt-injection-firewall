# Спецификация prompt-analysis

## Purpose

Определяет, как сервис анализирует prompts на признаки prompt injection, jailbreak, скрытых инструкций и подозрительных способов доставки инструкций до LLM.

## Requirements

### Requirement: Анализ одного prompt

Система SHALL принимать запрос на анализ одного prompt и возвращать оценку риска.

#### Scenario: Безопасный prompt разрешается

- GIVEN prompt не содержит подозрительных инструкций
- WHEN prompt анализируется
- THEN система SHALL вернуть риск `LOW`
- AND система SHALL вернуть решение `ALLOW`

#### Scenario: Prompt с переопределением инструкций блокируется

- GIVEN prompt просит модель игнорировать предыдущие инструкции
- WHEN prompt анализируется
- THEN система SHALL добавить reason `instruction_override`
- AND система SHALL вернуть риск `HIGH`
- AND система SHALL вернуть решение `BLOCK`

#### Scenario: Попытка извлечь system prompt блокируется

- GIVEN prompt просит раскрыть hidden, system, developer или internal instructions
- WHEN prompt анализируется
- THEN система SHALL добавить reason `system_prompt_extraction`
- AND система SHALL вернуть риск `HIGH`
- AND система SHALL вернуть решение `BLOCK`

### Requirement: Пакетный анализ prompts

Система SHALL поддерживать анализ нескольких prompts в одном запросе.

#### Scenario: Batch сохраняет порядок элементов

- GIVEN batch-запрос содержит несколько prompt items
- WHEN batch анализируется
- THEN ответ SHALL содержать один результат на каждый входной элемент
- AND результаты SHALL возвращаться в том же порядке, что и входные элементы

### Requirement: Prompt analyzers

Система SHALL анализировать prompts независимыми анализаторами.

#### Scenario: Эвристические анализаторы запускаются для каждого запроса

- GIVEN запрос анализа prompt валиден
- WHEN prompt анализируется
- THEN jailbreak phrase analyzer SHALL быть запущен
- AND system prompt leak analyzer SHALL быть запущен
- AND encoding obfuscation analyzer SHALL быть запущен
- AND URL instruction analyzer SHALL быть запущен
- AND markdown injection analyzer SHALL быть запущен

### Requirement: Отказоустойчивость анализаторов

Система SHALL выдерживать сбои отдельных анализаторов.

#### Scenario: Сбой анализатора не ломает запрос

- GIVEN один анализатор завершился ошибкой во время обработки prompt
- WHEN анализ prompt завершается
- THEN система SHALL вернуть валидную оценку риска
- AND система SHALL включить signals, полученные от успешно отработавших анализаторов

### Requirement: Конкурентное выполнение анализаторов

Система SHALL запускать независимые анализаторы конкурентно через `CompletableFuture`.

#### Scenario: Analyzer futures объединяются

- GIVEN доступно несколько анализаторов
- WHEN prompt анализируется
- THEN выполнение анализаторов SHALL быть представлено операциями `CompletableFuture`
- AND финальный ответ SHALL формироваться после объединения analyzer futures
