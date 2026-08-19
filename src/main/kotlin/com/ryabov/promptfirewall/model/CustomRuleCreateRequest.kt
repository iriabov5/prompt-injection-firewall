package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * Request body для создания пользовательского правила анализа prompt.
 */
@Serdeable
@Schema(description = "Запрос создания custom rule")
data class CustomRuleCreateRequest(
    /** Машинно-читаемый код сигнала, который появится в response при совпадении. */
    @field:NotBlank
    @field:Size(max = 64)
    @field:Schema(description = "Код risk signal", example = "company_secret_leak")
    val code: String,
    /** Тип matching: phrase или regex. */
    @field:NotNull
    @field:Schema(description = "Тип custom rule", example = "PHRASE")
    val type: CustomRuleType,
    /** Фраза для `PHRASE` rule. */
    @field:Size(max = 512)
    @field:Schema(description = "Фраза для PHRASE rule", example = "internal token", nullable = true)
    val phrase: String? = null,
    /** Regex pattern для `REGEX` rule. */
    @field:Size(max = 512)
    @field:Schema(description = "Regex pattern для REGEX rule", example = "AKIA[0-9A-Z]{16}", nullable = true)
    val pattern: String? = null,
    /** Вклад custom rule в общий risk score. */
    @field:Min(1)
    @field:Max(100)
    @field:Schema(description = "Вес custom rule в risk score", example = "45")
    val weight: Int,
    /** Человекочитаемое описание найденного риска. */
    @field:NotBlank
    @field:Size(max = 256)
    @field:Schema(description = "Описание custom rule")
    val description: String,
    /** Позволяет хранить правило, но временно не учитывать его при анализе. */
    @field:Schema(description = "Флаг активности custom rule", example = "true")
    val enabled: Boolean = true
)
