package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Объяснимый признак риска, найденный одним из анализаторов prompt.
 */
@Serdeable
@Schema(description = "Объяснимый признак риска")
data class RiskSignal(
    /** Машинно-читаемый код причины, используемый в API и тестах. */
    @field:Schema(description = "Машинно-читаемый код причины", example = "instruction_override")
    val code: String,
    /** Вклад сигнала в общий risk score до нормализации. */
    @field:Schema(description = "Вес сигнала в risk score", example = "35")
    val weight: Int,
    /** Человекочитаемое описание найденного признака. */
    @field:Schema(description = "Описание найденного признака")
    val description: String
)
