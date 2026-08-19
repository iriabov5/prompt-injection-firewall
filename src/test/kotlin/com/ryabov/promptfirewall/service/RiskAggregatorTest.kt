package com.ryabov.promptfirewall.service

import com.ryabov.promptfirewall.model.Decision
import com.ryabov.promptfirewall.model.RiskLevel
import com.ryabov.promptfirewall.model.RiskSignal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Агрегация risk signals в итоговое решение")
class RiskAggregatorTest {

    private val aggregator = RiskAggregator()

    @Test
    @DisplayName("Маппит score ниже review-порога в LOW risk")
    fun `maps scores below review threshold to low risk`() {
        assertEquals(RiskLevel.LOW, aggregator.riskLevel(29))
    }

    @Test
    @DisplayName("Маппит review-порог в MEDIUM risk")
    fun `maps review threshold to medium risk`() {
        assertEquals(RiskLevel.MEDIUM, aggregator.riskLevel(30))
    }

    @Test
    @DisplayName("Маппит block-порог в HIGH risk")
    fun `maps block threshold to high risk`() {
        assertEquals(RiskLevel.HIGH, aggregator.riskLevel(60))
    }

    @Test
    @DisplayName("Маппит LOW risk в решение ALLOW")
    fun `maps low risk to allow decision`() {
        assertEquals(Decision.ALLOW, aggregator.decision(RiskLevel.LOW))
    }

    @Test
    @DisplayName("Маппит MEDIUM risk в решение REVIEW")
    fun `maps medium risk to review decision`() {
        assertEquals(Decision.REVIEW, aggregator.decision(RiskLevel.MEDIUM))
    }

    @Test
    @DisplayName("Маппит HIGH risk в решение BLOCK")
    fun `maps high risk to block decision`() {
        assertEquals(Decision.BLOCK, aggregator.decision(RiskLevel.HIGH))
    }

    @Test
    @DisplayName("Собирает score, reasons, signals и AI summary в один response")
    fun `aggregates score reasons signals and ai summary`() {
        val first = RiskSignal(
            code = "instruction_override",
            weight = 35,
            description = "Prompt asks to ignore previous instructions"
        )
        val duplicate = RiskSignal(
            code = "instruction_override",
            weight = 10,
            description = "Another override signal"
        )
        val second = RiskSignal(
            code = "system_prompt_extraction",
            weight = 40,
            description = "Prompt asks to reveal system instructions"
        )

        val response = aggregator.aggregate(
            signals = listOf(first, duplicate, second),
            latencyMs = 12,
            aiSummary = "High-risk prompt"
        )

        assertEquals(85, response.score)
        assertEquals(RiskLevel.HIGH, response.risk)
        assertEquals(Decision.BLOCK, response.decision)
        assertEquals(listOf("instruction_override", "system_prompt_extraction"), response.reasons)
        assertEquals(listOf(first, duplicate, second), response.signals)
        assertEquals("High-risk prompt", response.aiSummary)
        assertEquals(12, response.latencyMs)
    }

    @Test
    @DisplayName("Нормализует суммарный score до 100")
    fun `normalizes aggregated score to one hundred`() {
        val response = aggregator.aggregate(
            signals = listOf(
                RiskSignal("first", 80, "First signal"),
                RiskSignal("second", 80, "Second signal")
            ),
            latencyMs = 3
        )

        assertEquals(100, response.score)
        assertEquals(RiskLevel.HIGH, response.risk)
        assertEquals(Decision.BLOCK, response.decision)
    }

    @Test
    @DisplayName("Возвращает LOW/ALLOW для пустого списка signals")
    fun `aggregates empty signals as low risk allow`() {
        val response = aggregator.aggregate(signals = emptyList(), latencyMs = 1)

        assertEquals(0, response.score)
        assertEquals(RiskLevel.LOW, response.risk)
        assertEquals(Decision.ALLOW, response.decision)
        assertEquals(emptyList<String>(), response.reasons)
        assertEquals(emptyList<RiskSignal>(), response.signals)
    }

    @Test
    @DisplayName("Отклоняет некорректный порядок порогов")
    fun `rejects invalid threshold range`() {
        assertThrows(IllegalArgumentException::class.java) {
            RiskAggregator(reviewThreshold = 60, blockThreshold = 30)
        }
    }
}
