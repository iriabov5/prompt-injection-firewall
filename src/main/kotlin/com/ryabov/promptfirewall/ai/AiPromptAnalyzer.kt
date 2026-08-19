package com.ryabov.promptfirewall.ai

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * Опциональный analyzer, который добавляет AI-assisted risk signal поверх эвристик.
 */
@Singleton
@Requires(property = "ai.enabled", value = "true")
class AiPromptAnalyzer(
    private val aiClient: AiClient,
    private val aiProperties: AiProperties
) : PromptRiskAnalyzer {

    /**
     * Возвращает один AI signal при положительном score; при ошибке или timeout возвращает пустой список.
     */
    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        aiClient
            .analyze(request.prompt)
            .completeOnTimeout(null, aiProperties.timeoutMs, TimeUnit.MILLISECONDS)
            .exceptionally { null }
            .thenApply(::toSignals)

    private fun toSignals(result: AiAnalysisResult?): List<RiskSignal> =
        if (result == null || result.score <= 0) {
            emptyList()
        } else {
            listOf(
                RiskSignal(
                    code = "ai_assisted_risk",
                    weight = result.score,
                    description = result.summary ?: result.reason
                )
            )
        }
}
