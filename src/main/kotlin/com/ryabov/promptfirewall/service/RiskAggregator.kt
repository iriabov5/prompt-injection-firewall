package com.ryabov.promptfirewall.service

import com.ryabov.promptfirewall.model.Decision
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import com.ryabov.promptfirewall.model.RiskLevel
import com.ryabov.promptfirewall.model.RiskSignal

/**
 * Преобразует набор risk signals в итоговый score, risk level, decision и
 * объяснимые reasons, которые возвращаются наружу через API.
 */
class RiskAggregator(
    private val scoreNormalizer: ScoreNormalizer = ScoreNormalizer(),
    private val reviewThreshold: Int = DEFAULT_REVIEW_THRESHOLD,
    private val blockThreshold: Int = DEFAULT_BLOCK_THRESHOLD
) {

    init {
        require(reviewThreshold in ScoreNormalizer.MIN_SCORE..ScoreNormalizer.MAX_SCORE) {
            "reviewThreshold must be between 0 and 100"
        }
        require(blockThreshold in ScoreNormalizer.MIN_SCORE..ScoreNormalizer.MAX_SCORE) {
            "blockThreshold must be between 0 and 100"
        }
        require(reviewThreshold < blockThreshold) {
            "reviewThreshold must be lower than blockThreshold"
        }
    }

    /**
     * Суммирует веса signals, нормализует итоговый score и формирует ответ анализа.
     * Reasons возвращаются в стабильном порядке, чтобы HTTP-ответы и тесты были детерминированными.
     */
    fun aggregate(
        signals: List<RiskSignal>,
        latencyMs: Long,
        aiSummary: String? = null
    ): PromptAnalyzeResponse {
        val score = scoreNormalizer.normalize(signals.sumOf { it.weight })
        val risk = riskLevel(score)

        return PromptAnalyzeResponse(
            risk = risk,
            score = score,
            decision = decision(risk),
            reasons = signals.map { it.code }.distinct().sorted(),
            signals = signals,
            aiSummary = aiSummary,
            latencyMs = latencyMs
        )
    }

    /**
     * Определяет уровень риска по настроенным порогам review/block.
     */
    fun riskLevel(score: Int): RiskLevel {
        val normalizedScore = scoreNormalizer.normalize(score)

        return when {
            normalizedScore >= blockThreshold -> RiskLevel.HIGH
            normalizedScore >= reviewThreshold -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }

    /**
     * Маппит уровень риска в решение firewall для вызывающей системы.
     */
    fun decision(riskLevel: RiskLevel): Decision = when (riskLevel) {
        RiskLevel.LOW -> Decision.ALLOW
        RiskLevel.MEDIUM -> Decision.REVIEW
        RiskLevel.HIGH -> Decision.BLOCK
    }

    companion object {
        const val DEFAULT_REVIEW_THRESHOLD = 30
        const val DEFAULT_BLOCK_THRESHOLD = 60
    }
}
