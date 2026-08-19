package com.ryabov.promptfirewall.service

import com.ryabov.promptfirewall.ai.AiProperties
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import jakarta.inject.Singleton
import java.util.concurrent.TimeUnit

/**
 * Записывает bounded Micrometer-метрики по итогам анализа prompt без изменения HTTP-контракта.
 */
@Singleton
class PromptAnalysisMetrics(
    private val meterRegistry: MeterRegistry,
    private val aiProperties: AiProperties
) {

    /**
     * Фиксирует counter и latency timer для одного проанализированного prompt item.
     */
    fun record(
        request: PromptAnalyzeRequest,
        response: PromptAnalyzeResponse
    ) {
        val tags = tags(request, response)

        meterRegistry.counter(ANALYSIS_TOTAL, tags).increment()
        meterRegistry.timer(ANALYSIS_LATENCY, tags).record(response.latencyMs.coerceAtLeast(0), TimeUnit.MILLISECONDS)
    }

    private fun tags(
        request: PromptAnalyzeRequest,
        response: PromptAnalyzeResponse
    ): Tags = Tags.of(
        "decision", response.decision.name,
        "risk", response.risk.name,
        "source", sourceTag(request.source),
        "ai_mode", aiMode(),
        "ai_outcome", aiOutcome(response)
    )

    private fun sourceTag(source: String?): String =
        source
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: UNKNOWN_SOURCE

    private fun aiMode(): String =
        if (aiProperties.enabled) AI_MODE_ENABLED else AI_MODE_DISABLED

    private fun aiOutcome(response: PromptAnalyzeResponse): String =
        if (!aiProperties.enabled) {
            AI_OUTCOME_SKIPPED
        } else if (response.aiSummary != null || response.signals.any { it.code == AI_ASSISTED_RISK_SIGNAL }) {
            AI_OUTCOME_SUCCESS
        } else {
            AI_OUTCOME_FAILED
        }

    companion object {
        const val ANALYSIS_TOTAL = "prompt_firewall_analysis_total"
        const val ANALYSIS_LATENCY = "prompt_firewall_analysis_latency"

        private const val UNKNOWN_SOURCE = "unknown"
        private const val AI_MODE_DISABLED = "disabled"
        private const val AI_MODE_ENABLED = "enabled"
        private const val AI_OUTCOME_SKIPPED = "skipped"
        private const val AI_OUTCOME_SUCCESS = "success"
        private const val AI_OUTCOME_FAILED = "failed"
        private const val AI_ASSISTED_RISK_SIGNAL = "ai_assisted_risk"
    }
}
