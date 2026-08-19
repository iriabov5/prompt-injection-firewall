package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.model.CustomRuleType

/**
 * Внутреннее представление custom rule, уже прошедшего validation и готового
 * к применению в analyzer pipeline.
 */
data class CustomRule(
    val id: String,
    val code: String,
    val type: CustomRuleType,
    val phrase: String?,
    val pattern: String?,
    val weight: Int,
    val description: String,
    val enabled: Boolean
)
