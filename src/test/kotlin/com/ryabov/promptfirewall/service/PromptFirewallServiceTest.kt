package com.ryabov.promptfirewall.service

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.Decision
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import java.time.Duration
import java.util.concurrent.CompletableFuture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PromptFirewallServiceTest {

    @Test
    fun `combines analyzer futures into one response`() {
        val service = PromptFirewallService(
            analyzers = listOf(
                fixedAnalyzer(RiskSignal("instruction_override", 35, "Override")),
                fixedAnalyzer(RiskSignal("system_prompt_extraction", 40, "Leak"))
            )
        )

        val response = service.analyze(PromptAnalyzeRequest("bad prompt")).join()

        assertEquals(75, response.score)
        assertEquals(Decision.BLOCK, response.decision)
        assertEquals(listOf("instruction_override", "system_prompt_extraction"), response.reasons)
        assertEquals(2, response.signals.size)
        assertTrue(response.latencyMs >= 0)
    }

    @Test
    fun `ignores failed analyzer and returns successful signals`() {
        val service = PromptFirewallService(
            analyzers = listOf(
                failingAnalyzer(),
                fixedAnalyzer(RiskSignal("safe_signal", 10, "Low signal"))
            )
        )

        val response = service.analyze(PromptAnalyzeRequest("mixed prompt")).join()

        assertEquals(10, response.score)
        assertEquals(Decision.ALLOW, response.decision)
        assertEquals(listOf("safe_signal"), response.reasons)
    }

    @Test
    fun `times out slow analyzer`() {
        val service = PromptFirewallService(
            analyzers = listOf(
                slowAnalyzer(),
                fixedAnalyzer(RiskSignal("fast_signal", 30, "Fast signal"))
            ),
            analyzerTimeout = Duration.ofMillis(25)
        )

        val response = service.analyze(PromptAnalyzeRequest("slow prompt")).join()

        assertEquals(30, response.score)
        assertEquals(Decision.REVIEW, response.decision)
        assertEquals(listOf("fast_signal"), response.reasons)
    }

    private fun fixedAnalyzer(signal: RiskSignal): PromptRiskAnalyzer =
        object : PromptRiskAnalyzer {
            override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
                CompletableFuture.completedFuture(listOf(signal))
        }

    private fun failingAnalyzer(): PromptRiskAnalyzer =
        object : PromptRiskAnalyzer {
            override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
                CompletableFuture.failedFuture(IllegalStateException("boom"))
        }

    private fun slowAnalyzer(): PromptRiskAnalyzer =
        object : PromptRiskAnalyzer {
            override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
                CompletableFuture.supplyAsync {
                    Thread.sleep(250)
                    listOf(RiskSignal("slow_signal", 60, "Slow signal"))
                }
        }
}
