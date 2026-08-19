package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

/**
 * Категория риска после нормализации score и применения настроенных порогов.
 */
@Serdeable
enum class RiskLevel {
    /** Низкий риск: признаков атаки нет или они слабо выражены. */
    LOW,
    /** Средний риск: есть сигналы, требующие review. */
    MEDIUM,
    /** Высокий риск: prompt похож на попытку обхода или раскрытия инструкций. */
    HIGH
}
