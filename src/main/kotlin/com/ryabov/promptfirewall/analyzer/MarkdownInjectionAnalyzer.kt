package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture

/**
 * Проверяет Markdown/HTML-разметку на скрытые инструкции, поддельные role markers
 * и ссылки, в тексте которых спрятаны управляющие команды.
 */
@Singleton
class MarkdownInjectionAnalyzer : PromptRiskAnalyzer {

    override fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>> =
        CompletableFuture.completedFuture(buildSignals(request.prompt))

    private fun buildSignals(prompt: String): List<RiskSignal> {
        val signals = mutableListOf<RiskSignal>()

        if (htmlCommentPattern.containsMatchIn(prompt)) {
            signals += RiskSignal(
                code = "hidden_markdown_instruction",
                weight = 20,
                description = "Prompt contains HTML comments that may hide instructions"
            )
        }

        if (roleMarkerPattern.containsMatchIn(prompt)) {
            signals += RiskSignal(
                code = "fake_role_marker",
                weight = 25,
                description = "Prompt contains fake role markers that may override conversation roles"
            )
        }

        if (markdownLinkInstructionPattern.containsMatchIn(prompt)) {
            signals += RiskSignal(
                code = "markdown_link_instruction",
                weight = 20,
                description = "Prompt contains markdown link text with embedded instructions"
            )
        }

        return signals
    }

    private companion object {
        val htmlCommentPattern = Regex("<!--.*?(ignore|system prompt|developer instructions).*?-->", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL))
        val roleMarkerPattern = Regex("(^|\\n)\\s*(system|developer|assistant)\\s*:", RegexOption.IGNORE_CASE)
        val markdownLinkInstructionPattern = Regex("\\[[^]]*(ignore|system prompt|developer instructions)[^]]*]\\([^)]*\\)", RegexOption.IGNORE_CASE)
    }
}
