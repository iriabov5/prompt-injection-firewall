package com.ryabov.promptfirewall.ai

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture

@DisplayName("AI analyzer для prompt firewall")
class AiPromptAnalyzerTest {

    @Test
    @DisplayName("Преобразует положительный AI score в risk signal")
    fun `maps positive ai result to risk signal`() {
        val analyzer = AiPromptAnalyzer(
            aiClient = FakeAiClient(
                CompletableFuture.completedFuture(
                    AiAnalysisResult(
                        score = 70,
                        reason = "model_detected_injection",
                        summary = "AI нашел попытку prompt injection"
                    )
                )
            ),
            aiProperties = AiProperties()
        )

        val signals = analyzer.analyze(PromptAnalyzeRequest("suspicious prompt")).join()

        assertEquals("ai_assisted_risk", signals.single().code)
        assertEquals(70, signals.single().weight)
        assertEquals("AI нашел попытку prompt injection", signals.single().description)
    }

    @Test
    @DisplayName("Не возвращает signals для нулевого AI score")
    fun `returns no signals for zero ai score`() {
        val analyzer = AiPromptAnalyzer(
            aiClient = FakeAiClient(CompletableFuture.completedFuture(AiAnalysisResult(0, "safe"))),
            aiProperties = AiProperties()
        )

        val signals = analyzer.analyze(PromptAnalyzeRequest("regular prompt")).join()

        assertTrue(signals.isEmpty())
    }

    @Test
    @DisplayName("Возвращает пустой список signals при ошибке AI provider")
    fun `returns no signals when ai client fails`() {
        val analyzer = AiPromptAnalyzer(
            aiClient = FakeAiClient(CompletableFuture.failedFuture(IllegalStateException("provider failed"))),
            aiProperties = AiProperties()
        )

        val signals = analyzer.analyze(PromptAnalyzeRequest("any prompt")).join()

        assertTrue(signals.isEmpty())
    }

    @Test
    @DisplayName("Возвращает пустой список signals при timeout AI provider")
    fun `returns no signals when ai client times out`() {
        val properties = AiProperties().apply {
            timeoutMs = 25
        }
        val analyzer = AiPromptAnalyzer(
            aiClient = FakeAiClient(CompletableFuture()),
            aiProperties = properties
        )

        val signals = analyzer.analyze(PromptAnalyzeRequest("any prompt")).join()

        assertTrue(signals.isEmpty())
    }
}
