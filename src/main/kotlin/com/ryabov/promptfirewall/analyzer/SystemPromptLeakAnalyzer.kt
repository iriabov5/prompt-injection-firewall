package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture

@Singleton
class SystemPromptLeakAnalyzer : PromptRiskAnalyzer {

    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        CompletableFuture.completedFuture(
            if (patterns.any { it.containsMatchIn(request.prompt) }) {
                listOf(
                    RiskSignal(
                        code = "system_prompt_extraction",
                        weight = 40,
                        description = "Prompt attempts to reveal hidden system or developer instructions"
                    )
                )
            } else {
                emptyList()
            }
        )

    private companion object {
        val patterns = listOf(
            Regex("\\b(show|print|reveal|display)\\s+(your\\s+)?(system|developer|hidden|internal)\\s+(prompt|instructions|rules|policy)\\b", RegexOption.IGNORE_CASE),
            Regex("\\bwhat\\s+are\\s+your\\s+(system|developer|hidden|internal)\\s+(instructions|rules)\\b", RegexOption.IGNORE_CASE),
            Regex("\\binitial\\s+prompt\\b", RegexOption.IGNORE_CASE)
        )
    }
}
