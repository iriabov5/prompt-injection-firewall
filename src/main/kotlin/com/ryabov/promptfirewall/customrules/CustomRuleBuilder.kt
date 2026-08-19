package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.model.CustomRuleCreateRequest
import com.ryabov.promptfirewall.model.CustomRuleType

/**
 * Минимальный builder с lambda receiver для создания validated rule state из
 * HTTP request и компактных test fixtures без повторения длинных конструкторов.
 */
class CustomRuleBuilder {
    var id: String = ""
    var code: String = ""
    var type: CustomRuleType = CustomRuleType.PHRASE
    var phrase: String? = null
    var pattern: String? = null
    var weight: Int = 1
    var description: String = ""
    var enabled: Boolean = true

    fun from(request: CustomRuleCreateRequest) {
        code = request.code.trim()
        type = request.type
        phrase = request.phrase?.trim()
        pattern = request.pattern?.trim()
        weight = request.weight
        description = request.description.trim()
        enabled = request.enabled
    }

    fun build(): CustomRule =
        CustomRule(
            id = id,
            code = code,
            type = type,
            phrase = phrase,
            pattern = pattern,
            weight = weight,
            description = description,
            enabled = enabled
        )
}

/**
 * Создает custom rule через Kotlin lambda with receiver.
 */
fun customRule(block: CustomRuleBuilder.() -> Unit): CustomRule =
    CustomRuleBuilder()
        .apply(block)
        .build()
