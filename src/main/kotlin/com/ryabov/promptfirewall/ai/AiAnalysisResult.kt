package com.ryabov.promptfirewall.ai

import io.micronaut.serde.annotation.Serdeable

/**
 * Нормализованный результат AI-assisted анализа, который затем превращается в risk signal.
 */
@Serdeable
data class AiAnalysisResult(
    /** Оценка риска от AI provider в диапазоне 0..100. */
    val score: Int,
    /** Короткая машинно-читаемая или человекочитаемая причина риска. */
    val reason: String,
    /** Краткое объяснение, которое можно показать оператору или сохранить в аудите. */
    val summary: String? = null
)
