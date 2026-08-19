package com.ryabov.promptfirewall.security

import io.micronaut.http.HttpRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

@DisplayName("Валидация API key token")
class ApiKeyTokenValidatorTest {

    @Test
    @DisplayName("Аутентифицирует валидный API key")
    fun `authenticates valid api key`() {
        val validator = ApiKeyTokenValidator(properties("test-secret"))

        val request = HttpRequest.GET<Any>("/api/v1/prompts/analyze")
            .header("X-API-Key", "test-secret")
        val authentication = Mono.from(validator.validateToken("test-secret", request)).block()

        assertEquals("api-key-client", authentication!!.name)
    }

    @Test
    @DisplayName("Не принимает валидный secret из другого header")
    fun `rejects valid secret from another header`() {
        val validator = ApiKeyTokenValidator(properties("test-secret"))
        val request = HttpRequest.GET<Any>("/api/v1/prompts/analyze")
            .bearerAuth("test-secret")

        val authentication = Mono.from(validator.validateToken("test-secret", request)).blockOptional()

        assertTrue(authentication.isEmpty)
    }

    @Test
    @DisplayName("Отклоняет неверный API key")
    fun `rejects invalid api key`() {
        val validator = ApiKeyTokenValidator(properties("test-secret"))

        val authentication = Mono.from(validator.validateToken("wrong-secret", null)).blockOptional()

        assertTrue(authentication.isEmpty)
    }

    @Test
    @DisplayName("Не считает пустой configured key валидным")
    fun `rejects blank configured api key`() {
        val validator = ApiKeyTokenValidator(properties(""))

        val authentication = Mono.from(validator.validateToken("", null)).blockOptional()

        assertTrue(authentication.isEmpty)
    }

    private fun properties(vararg keys: String): ApiKeySecurityProperties {
        val properties = ApiKeySecurityProperties()
        properties.keys = keys.toList()
        return properties
    }
}
