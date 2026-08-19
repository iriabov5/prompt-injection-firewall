package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.configuration.CustomRulesProperties
import com.ryabov.promptfirewall.model.CustomRuleCreateRequest
import com.ryabov.promptfirewall.model.CustomRuleType
import io.micronaut.http.exceptions.HttpStatusException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("In-memory registry custom rules")
class CustomRuleRegistryTest {

    @Test
    @DisplayName("Сохраняет rules в порядке создания")
    fun `stores rules in creation order`() {
        val registry = CustomRuleRegistry(CustomRulesProperties())

        val first = registry.create(phraseRequest(code = "first", phrase = "one"))
        val second = registry.create(phraseRequest(code = "second", phrase = "two"))

        assertEquals(listOf(first.id, second.id), registry.list().map(CustomRule::id))
    }

    @Test
    @DisplayName("Удаляет существующее rule и не падает при повторном удалении")
    fun `deletes rules idempotently`() {
        val registry = CustomRuleRegistry(CustomRulesProperties())
        val rule = registry.create(phraseRequest(code = "first", phrase = "one"))

        registry.delete(rule.id)
        registry.delete(rule.id)

        assertTrue(registry.list().isEmpty())
    }

    @Test
    @DisplayName("Отклоняет создание rule сверх configured limit")
    fun `rejects rule when limit is reached`() {
        val properties = CustomRulesProperties().apply { maxRules = 1 }
        val registry = CustomRuleRegistry(properties)
        registry.create(phraseRequest(code = "first", phrase = "one"))

        assertThrows(HttpStatusException::class.java) {
            registry.create(phraseRequest(code = "second", phrase = "two"))
        }
        assertEquals(1, registry.list().size)
    }

    @Test
    @DisplayName("Отклоняет phrase rule без phrase")
    fun `rejects phrase rule without phrase`() {
        val registry = CustomRuleRegistry(CustomRulesProperties())

        assertThrows(HttpStatusException::class.java) {
            registry.create(phraseRequest(phrase = " "))
        }
    }

    @Test
    @DisplayName("Отклоняет regex rule с некорректным pattern")
    fun `rejects invalid regex rule`() {
        val registry = CustomRuleRegistry(CustomRulesProperties())

        assertThrows(HttpStatusException::class.java) {
            registry.create(
                CustomRuleCreateRequest(
                    code = "bad_regex",
                    type = CustomRuleType.REGEX,
                    pattern = "[",
                    weight = 40,
                    description = "Invalid regex"
                )
            )
        }
    }

    private fun phraseRequest(
        code: String = "custom_rule",
        phrase: String = "internal token"
    ): CustomRuleCreateRequest =
        CustomRuleCreateRequest(
            code = code,
            type = CustomRuleType.PHRASE,
            phrase = phrase,
            weight = 35,
            description = "Prompt contains forbidden phrase"
        )
}
