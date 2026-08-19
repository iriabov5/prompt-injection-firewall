package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture

/**
 * Analyzer, который применяет enabled custom rules к prompt и возвращает
 * найденные совпадения как обычные risk signals общего pipeline.
 */
@Singleton
class CustomRuleAnalyzer(
    private val customRuleRegistry: CustomRuleRegistry
) : PromptRiskAnalyzer {

    /**
     * Выполняет matching rules синхронно внутри уже асинхронного analyzer pipeline.
     */
    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        CompletableFuture.completedFuture(
            customRuleRegistry
                .list()
                .filter { rule -> rule.matches(request.prompt) }
                .map(CustomRule::toRiskSignal)
        )
}
