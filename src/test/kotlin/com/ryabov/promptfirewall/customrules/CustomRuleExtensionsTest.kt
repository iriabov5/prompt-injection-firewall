package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.model.CustomRuleType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Custom rule extension functions")
class CustomRuleExtensionsTest {

    @Test
    @DisplayName("Нормализует текст для case-insensitive matching")
    fun `normalizes prompt for rule matching`() {
        assertEquals("internal token", "  Internal Token  ".normalizedForRuleMatching())
    }

    @Test
    @DisplayName("Находит phrase rule независимо от регистра")
    fun `matches phrase rule case insensitively`() {
        val rule = customRule {
            id = "rule-1"
            code = "internal_token"
            phrase = "Internal Token"
            weight = 40
            description = "Prompt contains internal token"
        }

        assertTrue(rule.matches("Please check this internal token before sending"))
    }

    @Test
    @DisplayName("Находит regex rule по pattern")
    fun `matches regex rule`() {
        val rule = customRule {
            id = "rule-1"
            code = "aws_key"
            type = CustomRuleType.REGEX
            pattern = "AKIA[0-9A-Z]{16}"
            weight = 70
            description = "Prompt contains AWS key"
        }

        assertTrue(rule.matches("secret: AKIA1234567890ABCDEF"))
    }

    @Test
    @DisplayName("Игнорирует disabled rule даже при совпадении")
    fun `does not match disabled rule`() {
        val rule = customRule {
            id = "rule-1"
            code = "disabled_rule"
            phrase = "blocked phrase"
            weight = 30
            description = "Disabled rule"
            enabled = false
        }

        assertFalse(rule.matches("blocked phrase"))
    }

    @Test
    @DisplayName("Преобразует custom rule в стандартный risk signal")
    fun `converts rule to risk signal`() {
        val rule = customRule {
            id = "rule-1"
            code = "company_secret"
            phrase = "secret"
            weight = 45
            description = "Prompt contains company secret"
        }

        val signal = rule.toRiskSignal()

        assertEquals("company_secret", signal.code)
        assertEquals(45, signal.weight)
        assertEquals("Prompt contains company secret", signal.description)
    }
}
