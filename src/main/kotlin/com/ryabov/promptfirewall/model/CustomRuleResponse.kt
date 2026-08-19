package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * HTTP response model пользовательского правила, сохраненного в in-memory registry.
 */
@Serdeable
@Schema(description = "Custom rule response")
data class CustomRuleResponse(
    /** Generated identifier правила внутри текущего процесса. */
    @field:Schema(description = "Generated identifier custom rule")
    val id: String,
    /** Машинно-читаемый код risk signal. */
    @field:Schema(description = "Код risk signal", example = "company_secret_leak")
    val code: String,
    /** Тип matching: phrase или regex. */
    @field:Schema(description = "Тип custom rule", example = "PHRASE")
    val type: CustomRuleType,
    /** Фраза для PHRASE rule. */
    @field:Schema(description = "Фраза для PHRASE rule", nullable = true)
    val phrase: String?,
    /** Regex pattern для REGEX rule. */
    @field:Schema(description = "Regex pattern для REGEX rule", nullable = true)
    val pattern: String?,
    /** Вклад custom rule в общий risk score. */
    @field:Schema(description = "Вес custom rule в risk score", example = "45")
    val weight: Int,
    /** Человекочитаемое описание риска. */
    @field:Schema(description = "Описание custom rule")
    val description: String,
    /** Флаг активности custom rule. */
    @field:Schema(description = "Флаг активности custom rule", example = "true")
    val enabled: Boolean
)
