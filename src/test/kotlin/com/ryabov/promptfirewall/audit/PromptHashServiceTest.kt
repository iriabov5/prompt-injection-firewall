package com.ryabov.promptfirewall.audit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Hashing prompt для audit log")
class PromptHashServiceTest {

    private val promptHashService = PromptHashService()

    @Test
    @DisplayName("Строит deterministic SHA-256 hash с префиксом алгоритма")
    fun `builds deterministic sha256 prompt hash`() {
        val first = promptHashService.hash("secret prompt")
        val second = promptHashService.hash("secret prompt")

        assertEquals(first, second)
        assertTrue(first.startsWith("sha256:"))
        assertEquals(71, first.length)
    }

    @Test
    @DisplayName("Возвращает разные hash для разных prompts")
    fun `returns different hashes for different prompts`() {
        assertNotEquals(
            promptHashService.hash("first prompt"),
            promptHashService.hash("second prompt")
        )
    }
}
