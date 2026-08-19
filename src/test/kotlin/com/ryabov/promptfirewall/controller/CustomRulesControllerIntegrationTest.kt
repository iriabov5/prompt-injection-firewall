package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.model.CustomRuleCreateRequest
import com.ryabov.promptfirewall.model.CustomRuleResponse
import com.ryabov.promptfirewall.model.CustomRuleType
import com.ryabov.promptfirewall.model.Decision
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("HTTP API пользовательских custom rules")
class CustomRulesControllerIntegrationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    @DisplayName("Отклоняет создание custom rule без API key")
    fun `rejects custom rule creation without api key`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/rules", phraseRequest("missing_key_rule", "secret")),
                String::class.java
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    @DisplayName("Создает, возвращает и удаляет phrase custom rule")
    fun `creates lists and deletes phrase custom rule`() {
        val created = createRule(phraseRequest("custom_phrase_${System.nanoTime()}", "internal token"))

        val listed = client.toBlocking().retrieve(
            HttpRequest.GET<Any>("/api/v1/rules").header(API_KEY_HEADER, TEST_API_KEY),
            Argument.listOf(CustomRuleResponse::class.java)
        )

        assertTrue(listed.any { rule -> rule.id == created.id })

        val deleteResponse = client.toBlocking().exchange(
            HttpRequest.DELETE<Any>("/api/v1/rules/${created.id}").header(API_KEY_HEADER, TEST_API_KEY),
            String::class.java
        )

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status)
    }

    @Test
    @DisplayName("Отклоняет regex custom rule с некорректным pattern")
    fun `rejects invalid regex custom rule`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                HttpRequest.POST(
                    "/api/v1/rules",
                    CustomRuleCreateRequest(
                        code = "bad_regex_${System.nanoTime()}",
                        type = CustomRuleType.REGEX,
                        pattern = "[",
                        weight = 40,
                        description = "Invalid regex"
                    )
                ).header(API_KEY_HEADER, TEST_API_KEY),
                String::class.java
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    @DisplayName("Учитывает custom rule при анализе prompt")
    fun `uses custom rule during prompt analysis`() {
        val code = "custom_secret_${System.nanoTime()}"
        createRule(phraseRequest(code, "confidential roadmap"))

        val response = client.toBlocking().retrieve(
            HttpRequest.POST(
                "/api/v1/prompts/analyze",
                PromptAnalyzeRequest("Please share the confidential roadmap")
            ).header(API_KEY_HEADER, TEST_API_KEY),
            PromptAnalyzeResponse::class.java
        )

        assertEquals(Decision.REVIEW, response.decision)
        assertTrue(response.reasons.contains(code))
        assertTrue(response.signals.any { signal -> signal.code == code })
    }

    @Test
    @DisplayName("Не учитывает disabled custom rule при анализе prompt")
    fun `ignores disabled custom rule during prompt analysis`() {
        val code = "disabled_secret_${System.nanoTime()}"
        createRule(phraseRequest(code, "disabled phrase", enabled = false))

        val response = client.toBlocking().retrieve(
            HttpRequest.POST(
                "/api/v1/prompts/analyze",
                PromptAnalyzeRequest("This prompt contains disabled phrase")
            ).header(API_KEY_HEADER, TEST_API_KEY),
            PromptAnalyzeResponse::class.java
        )

        assertFalse(response.reasons.contains(code))
    }

    private fun createRule(request: CustomRuleCreateRequest): CustomRuleResponse =
        client.toBlocking().retrieve(
            HttpRequest.POST("/api/v1/rules", request).header(API_KEY_HEADER, TEST_API_KEY),
            CustomRuleResponse::class.java
        )

    private fun phraseRequest(
        code: String,
        phrase: String,
        enabled: Boolean = true
    ): CustomRuleCreateRequest =
        CustomRuleCreateRequest(
            code = code,
            type = CustomRuleType.PHRASE,
            phrase = phrase,
            weight = 35,
            description = "Prompt contains custom phrase",
            enabled = enabled
        )

    private companion object {
        const val API_KEY_HEADER = "X-API-Key"
        const val TEST_API_KEY = "test-secret"
    }
}
