package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.model.CustomRuleResponse
import com.ryabov.promptfirewall.model.CustomRuleType
import com.ryabov.promptfirewall.model.RiskSignal

/**
 * Нормализует prompt и phrase для стабильного case-insensitive matching без
 * изменения исходного текста, который возвращается клиенту в API.
 */
fun String.normalizedForRuleMatching(): String =
    trim().lowercase()

/**
 * Проверяет, срабатывает ли custom rule на prompt. Disabled rules всегда
 * возвращают `false`, чтобы analyzer не создавал signals для выключенных правил.
 */
fun CustomRule.matches(prompt: String): Boolean {
    if (!enabled) {
        return false
    }

    return when (type) {
        CustomRuleType.PHRASE -> phrase
            ?.normalizedForRuleMatching()
            ?.let { normalizedPhrase -> normalizedPhrase in prompt.normalizedForRuleMatching() }
            ?: false

        CustomRuleType.REGEX -> pattern
            ?.let { Regex(it).containsMatchIn(prompt) }
            ?: false
    }
}

/**
 * Преобразует matched custom rule в стандартный объяснимый risk signal.
 */
fun CustomRule.toRiskSignal(): RiskSignal =
    RiskSignal(
        code = code,
        weight = weight,
        description = description
    )

/**
 * Преобразует internal rule state в публичный DTO без раскрытия runtime details registry.
 */
fun CustomRule.toResponse(): CustomRuleResponse =
    CustomRuleResponse(
        id = id,
        code = code,
        type = type,
        phrase = phrase,
        pattern = pattern,
        weight = weight,
        description = description,
        enabled = enabled
    )
