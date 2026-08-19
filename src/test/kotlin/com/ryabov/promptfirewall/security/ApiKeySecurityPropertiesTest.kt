package com.ryabov.promptfirewall.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Настройки API key security")
class ApiKeySecurityPropertiesTest {

    @Test
    @DisplayName("Исключает пустые configured keys из набора валидных ключей")
    fun `filters blank configured keys`() {
        val properties = ApiKeySecurityProperties()
        properties.keys = listOf("", "  ", "valid-key", " another-key ")

        assertEquals(setOf("valid-key", "another-key"), properties.validKeys())
        assertFalse("" in properties.validKeys())
    }
}
