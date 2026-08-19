package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

/**
 * Результат анализа prompt: риск, решение firewall, объяснения и диагностическая задержка.
 */
@Serdeable
data class PromptAnalyzeResponse(
    /** Уровень риска после применения scoring thresholds. */
    val risk: RiskLevel,
    /** Нормализованный score от 0 до 100. */
    val score: Int,
    /** Рекомендованное действие для вызывающей системы. */
    val decision: Decision,
    /** Уникальные коды причин, удобные для быстрого отображения и фильтрации. */
    val reasons: List<String>,
    /** Полные сигналы с весами и описаниями для объяснимости решения. */
    val signals: List<RiskSignal>,
    /** Опциональное AI-объяснение, если AI analyzer включен будущей конфигурацией. */
    val aiSummary: String? = null,
    /** Измеренная длительность анализа в миллисекундах. */
    val latencyMs: Long
)
