package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

/**
 * Объяснимый признак риска, найденный одним из анализаторов prompt.
 */
@Serdeable
data class RiskSignal(
    /** Машинно-читаемый код причины, используемый в API и тестах. */
    val code: String,
    /** Вклад сигнала в общий risk score до нормализации. */
    val weight: Int,
    /** Человекочитаемое описание найденного признака. */
    val description: String
)
