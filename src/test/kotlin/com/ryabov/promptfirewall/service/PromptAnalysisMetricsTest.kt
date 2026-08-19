package com.ryabov.promptfirewall.service

import com.ryabov.promptfirewall.ai.AiProperties
import com.ryabov.promptfirewall.model.Decision
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import com.ryabov.promptfirewall.model.RiskLevel
import com.ryabov.promptfirewall.model.RiskSignal
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Метрики анализа prompt")
class PromptAnalysisMetricsTest {

    private val meterRegistry = SimpleMeterRegistry()
    private val aiProperties = AiProperties()
    private val metrics = PromptAnalysisMetrics(meterRegistry, aiProperties)

    @Test
    @DisplayName("Записывает counter с decision, risk, source и AI tags")
    fun `records counter with bounded tags`() {
        metrics.record(
            PromptAnalyzeRequest("bad prompt", source = "chat"),
            response(
                decision = Decision.BLOCK,
                risk = RiskLevel.HIGH,
                signals = listOf(RiskSignal("instruction_override", 35, "Override"))
            )
        )

        val counter = meterRegistry
            .find(PromptAnalysisMetrics.ANALYSIS_TOTAL)
            .tags(
                "decision", "BLOCK",
                "risk", "HIGH",
                "source", "chat",
                "ai_mode", "disabled",
                "ai_outcome", "skipped"
            )
            .counter()

        assertNotNull(counter)
        assertEquals(1.0, counter!!.count())
    }

    @Test
    @DisplayName("Нормализует пустой source в unknown")
    fun `normalizes missing source tag`() {
        metrics.record(
            PromptAnalyzeRequest("safe prompt"),
            response()
        )

        val counter = meterRegistry
            .find(PromptAnalysisMetrics.ANALYSIS_TOTAL)
            .tag("source", "unknown")
            .counter()

        assertNotNull(counter)
        assertEquals(1.0, counter!!.count())
    }

    @Test
    @DisplayName("Записывает latency timer с теми же bounded tags")
    fun `records latency timer`() {
        metrics.record(
            PromptAnalyzeRequest("review prompt", source = "api"),
            response(decision = Decision.REVIEW, risk = RiskLevel.MEDIUM, latencyMs = 42)
        )

        val timer = meterRegistry
            .find(PromptAnalysisMetrics.ANALYSIS_LATENCY)
            .tags(
                "decision", "REVIEW",
                "risk", "MEDIUM",
                "source", "api",
                "ai_mode", "disabled",
                "ai_outcome", "skipped"
            )
            .timer()

        assertNotNull(timer)
        assertEquals(1, timer!!.count())
        assertEquals(42.0, timer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS))
    }

    @Test
    @DisplayName("Отмечает successful AI outcome по AI-assisted signal")
    fun `records successful ai outcome`() {
        aiProperties.enabled = true

        metrics.record(
            PromptAnalyzeRequest("ai prompt"),
            response(signals = listOf(RiskSignal("ai_assisted_risk", 25, "AI detected risk")))
        )

        val counter = meterRegistry
            .find(PromptAnalysisMetrics.ANALYSIS_TOTAL)
            .tag("ai_mode", "enabled")
            .tag("ai_outcome", "success")
            .counter()

        assertNotNull(counter)
        assertEquals(1.0, counter!!.count())
    }

    @Test
    @DisplayName("Отмечает failed AI outcome, когда включенный AI не дал наблюдаемого результата")
    fun `records failed ai outcome`() {
        aiProperties.enabled = true

        metrics.record(
            PromptAnalyzeRequest("ai prompt"),
            response()
        )

        val counter = meterRegistry
            .find(PromptAnalysisMetrics.ANALYSIS_TOTAL)
            .tag("ai_mode", "enabled")
            .tag("ai_outcome", "failed")
            .counter()

        assertNotNull(counter)
        assertEquals(1.0, counter!!.count())
    }

    private fun response(
        decision: Decision = Decision.ALLOW,
        risk: RiskLevel = RiskLevel.LOW,
        latencyMs: Long = 7,
        signals: List<RiskSignal> = emptyList()
    ): PromptAnalyzeResponse = PromptAnalyzeResponse(
        risk = risk,
        score = signals.sumOf { it.weight },
        decision = decision,
        reasons = signals.map { it.code },
        signals = signals,
        latencyMs = latencyMs
    )
}
