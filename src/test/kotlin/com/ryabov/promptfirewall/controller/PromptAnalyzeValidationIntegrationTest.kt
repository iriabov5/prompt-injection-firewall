package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.model.BatchPromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("HTTP validation для prompt analysis API")
class PromptAnalyzeValidationIntegrationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    @DisplayName("Отклоняет пустой prompt с BAD_REQUEST")
    fun `blank prompt is rejected`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                authenticated(HttpRequest.POST("/api/v1/prompts/analyze", PromptAnalyzeRequest(" "))),
                String::class.java
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    @DisplayName("Отклоняет prompt длиннее разрешенного лимита с BAD_REQUEST")
    fun `too long prompt is rejected`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                authenticated(HttpRequest.POST("/api/v1/prompts/analyze", PromptAnalyzeRequest("a".repeat(12001)))),
                String::class.java
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    @DisplayName("Отклоняет пустой batch с BAD_REQUEST")
    fun `empty batch is rejected`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                authenticated(HttpRequest.POST("/api/v1/prompts/analyze/batch", BatchPromptAnalyzeRequest(emptyList()))),
                String::class.java
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    private fun <T> authenticated(request: MutableHttpRequest<T>): HttpRequest<T> =
        request.header(API_KEY_HEADER, TEST_API_KEY)

    private companion object {
        const val API_KEY_HEADER = "X-API-Key"
        const val TEST_API_KEY = "test-secret"
    }
}
