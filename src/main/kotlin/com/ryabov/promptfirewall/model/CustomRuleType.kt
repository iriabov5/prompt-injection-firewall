package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Тип пользовательского правила: поиск фразы или проверка регулярным выражением.
 */
@Serdeable
@Schema(description = "Тип custom rule")
enum class CustomRuleType {
    PHRASE,
    REGEX
}
