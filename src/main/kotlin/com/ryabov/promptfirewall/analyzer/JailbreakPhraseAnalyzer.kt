package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture

/**
 * Находит явные jailbreak-фразы, которыми пользователь просит модель
 * игнорировать предыдущие инструкции или обойти правила безопасности.
 */
@Singleton
class JailbreakPhraseAnalyzer : PromptRiskAnalyzer {

    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        CompletableFuture.completedFuture(
            if (patterns.any { it.containsMatchIn(request.prompt) }) {
                listOf(
                    RiskSignal(
                        code = "instruction_override",
                        weight = 35,
                        description = "Prompt asks the model to ignore or override previous instructions"
                    )
                )
            } else {
                emptyList()
            }
        )

    private companion object {
        val patterns = listOf(
            Regex("\\bignore\\s+(all\\s+)?(previous|prior|above)\\s+instructions\\b", RegexOption.IGNORE_CASE),
            Regex("\\bforget\\s+(all\\s+)?(previous|prior|above)\\s+instructions\\b", RegexOption.IGNORE_CASE),
            Regex("\\bdeveloper\\s+mode\\b", RegexOption.IGNORE_CASE),
            Regex("\\bact\\s+as\\s+dan\\b", RegexOption.IGNORE_CASE),
            Regex("\\bbypass\\s+(the\\s+)?(policy|rules|safety)\\b", RegexOption.IGNORE_CASE)
        )
    }
}
