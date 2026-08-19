package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.configuration.CustomRulesProperties
import com.ryabov.promptfirewall.model.CustomRuleCreateRequest
import com.ryabov.promptfirewall.model.CustomRuleType
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Analyzer пользовательских custom rules")
class CustomRuleAnalyzerTest {

    @Test
    @DisplayName("Возвращает risk signal для matched custom rule")
    fun `returns risk signal for matched custom rule`() {
        val registry = CustomRuleRegistry(CustomRulesProperties())
        registry.create(
            CustomRuleCreateRequest(
                code = "company_secret",
                type = CustomRuleType.PHRASE,
                phrase = "internal token",
                weight = 45,
                description = "Prompt contains company secret"
            )
        )
        val analyzer = CustomRuleAnalyzer(registry)

        val signals = analyzer
            .analyze(PromptAnalyzeRequest("Please explain this internal token"))
            .join()

        assertEquals(1, signals.size)
        assertEquals("company_secret", signals.single().code)
        assertEquals(45, signals.single().weight)
    }

    @Test
    @DisplayName("Возвращает пустой список, если custom rules не совпали")
    fun `returns empty list when rules do not match`() {
        val registry = CustomRuleRegistry(CustomRulesProperties())
        registry.create(
            CustomRuleCreateRequest(
                code = "company_secret",
                type = CustomRuleType.PHRASE,
                phrase = "internal token",
                weight = 45,
                description = "Prompt contains company secret"
            )
        )
        val analyzer = CustomRuleAnalyzer(registry)

        val signals = analyzer
            .analyze(PromptAnalyzeRequest("Summarize this public text"))
            .join()

        assertTrue(signals.isEmpty())
    }
}
