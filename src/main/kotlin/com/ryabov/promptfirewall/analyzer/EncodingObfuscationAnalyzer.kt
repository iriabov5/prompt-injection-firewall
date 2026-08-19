package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import java.util.concurrent.CompletableFuture

class EncodingObfuscationAnalyzer : PromptRiskAnalyzer {

    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        CompletableFuture.completedFuture(buildSignals(request.prompt))

    private fun buildSignals(prompt: String): List<RiskSignal> {
        val signals = mutableListOf<RiskSignal>()

        if (base64LikePattern.containsMatchIn(prompt)) {
            signals += RiskSignal(
                code = "encoded_instruction",
                weight = 20,
                description = "Prompt contains a long base64-like fragment that may hide instructions"
            )
        }

        if (escapedPattern.containsMatchIn(prompt)) {
            signals += RiskSignal(
                code = "escaped_instruction",
                weight = 15,
                description = "Prompt contains escaped characters that may be used for obfuscation"
            )
        }

        if (invisibleCharsPattern.containsMatchIn(prompt)) {
            signals += RiskSignal(
                code = "hidden_text",
                weight = 15,
                description = "Prompt contains invisible characters that may hide instructions"
            )
        }

        return signals
    }

    private companion object {
        val base64LikePattern = Regex("\\b[A-Za-z0-9+/]{40,}={0,2}\\b")
        val escapedPattern = Regex("(\\\\u[0-9a-fA-F]{4}|%[0-9a-fA-F]{2}|0x[0-9a-fA-F]{2})")
        val invisibleCharsPattern = Regex("[\\u200B-\\u200F\\uFEFF]")
    }
}
