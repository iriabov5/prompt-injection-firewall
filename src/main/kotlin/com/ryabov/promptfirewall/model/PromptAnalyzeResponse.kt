package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Результат анализа prompt: риск, решение firewall, объяснения и диагностическая задержка.
 */
@Serdeable
@Schema(description = "Результат анализа prompt")
data class PromptAnalyzeResponse(
    /** Уровень риска после применения scoring thresholds. */
    @field:Schema(description = "Уровень риска")
    val risk: RiskLevel,
    /** Нормализованный score от 0 до 100. */
    @field:Schema(description = "Нормализованный risk score", minimum = "0", maximum = "100", example = "75")
    val score: Int,
    /** Рекомендованное действие для вызывающей системы. */
    @field:Schema(description = "Рекомендованное действие firewall")
    val decision: Decision,
    /** Уникальные коды причин, удобные для быстрого отображения и фильтрации. */
    @field:Schema(description = "Уникальные коды причин риска")
    val reasons: List<String>,
    /** Полные сигналы с весами и описаниями для объяснимости решения. */
    @field:Schema(description = "Детальные risk signals")
    val signals: List<RiskSignal>,
    /** Опциональное AI-объяснение, если AI analyzer включен будущей конфигурацией. */
    @field:Schema(description = "Опциональное AI-объяснение риска", nullable = true)
    val aiSummary: String? = null,
    /** Измеренная длительность анализа в миллисекундах. */
    @field:Schema(description = "Длительность анализа в миллисекундах", example = "24")
    val latencyMs: Long
)
