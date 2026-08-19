package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import jakarta.inject.Singleton
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

@Singleton
class UrlInstructionAnalyzer : PromptRiskAnalyzer {

    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        CompletableFuture.completedFuture(buildSignals(request.prompt))

    private fun buildSignals(prompt: String): List<RiskSignal> {
        val urls = urlPattern.findAll(prompt).map { it.value }.toList()
        if (urls.isEmpty()) {
            return emptyList()
        }

        val signals = mutableListOf<RiskSignal>()

        if (urls.any { it.length > LONG_URL_THRESHOLD }) {
            signals += RiskSignal(
                code = "long_url",
                weight = 10,
                description = "Prompt contains an unusually long URL"
            )
        }

        if (urls.any { url -> instructionTerms.any { decode(url).contains(it, ignoreCase = true) } }) {
            signals += RiskSignal(
                code = "url_embedded_instruction",
                weight = 25,
                description = "Prompt contains URL data that appears to embed instructions"
            )
        }

        if (urls.any { url -> redirectTerms.any { decode(url).contains(it, ignoreCase = true) } }) {
            signals += RiskSignal(
                code = "redirect_url",
                weight = 10,
                description = "Prompt contains a URL with redirect-like parameters"
            )
        }

        return signals
    }

    private fun decode(value: String): String =
        runCatching { URLDecoder.decode(value, StandardCharsets.UTF_8) }.getOrDefault(value)

    private companion object {
        const val LONG_URL_THRESHOLD = 160
        val urlPattern = Regex("https?://[^\\s)\\]>\"']+", RegexOption.IGNORE_CASE)
        val instructionTerms = listOf("ignore previous instructions", "system prompt", "developer instructions")
        val redirectTerms = listOf("redirect=", "redirect_uri=", "next=", "url=", "target=")
    }
}
