package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("API key security baseline")
class ApiKeySecurityIntegrationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    @DisplayName("Отклоняет analysis request без API key")
    fun `rejects analysis request without api key`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/prompts/analyze", PromptAnalyzeRequest("Summarize this text")),
                String::class.java
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    @DisplayName("Отклоняет analysis request с неверным API key")
    fun `rejects analysis request with invalid api key`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/prompts/analyze", PromptAnalyzeRequest("Summarize this text"))
                    .header(API_KEY_HEADER, "wrong-secret"),
                String::class.java
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
        assertFalse(exception.response.body.toString().contains(TEST_API_KEY))
    }

    @Test
    @DisplayName("Разрешает analysis request с валидным API key")
    fun `allows analysis request with valid api key`() {
        val response = client.toBlocking().retrieve(
            HttpRequest.POST("/api/v1/prompts/analyze", PromptAnalyzeRequest("Summarize this text"))
                .header(API_KEY_HEADER, TEST_API_KEY),
            PromptAnalyzeResponse::class.java
        )

        assertEquals(0, response.score)
    }

    @Test
    @DisplayName("Оставляет health endpoint доступным без API key")
    fun `allows anonymous health endpoint`() {
        val response = client.toBlocking().exchange(
            HttpRequest.GET<Any>("/api/v1/health"),
            String::class.java
        )

        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @DisplayName("Оставляет docs и metrics endpoints доступными по configured anonymous policy")
    fun `allows configured anonymous docs and metrics endpoints`() {
        val swagger = client.toBlocking().exchange(HttpRequest.GET<Any>("/swagger-ui/index.html"), String::class.java)
        val metrics = client.toBlocking().exchange(HttpRequest.GET<Any>("/metrics"), String::class.java)

        assertEquals(HttpStatus.OK, swagger.status)
        assertEquals(HttpStatus.OK, metrics.status)
    }

    @Test
    @DisplayName("Применяет baseline security headers")
    fun `applies baseline security headers`() {
        val response = client.toBlocking().exchange(
            HttpRequest.GET<Any>("/api/v1/health"),
            String::class.java
        )

        assertEquals("nosniff", response.header("X-Content-Type-Options"))
        assertEquals("DENY", response.header("X-Frame-Options"))
        assertEquals("no-referrer", response.header("Referrer-Policy"))
    }

    @Test
    @DisplayName("Применяет explicit CORS policy для preflight request")
    fun `applies explicit cors policy`() {
        val response = client.toBlocking().exchange(
            HttpRequest.create<Any>(HttpMethod.OPTIONS, "/api/v1/prompts/analyze")
                .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, API_KEY_HEADER),
            String::class.java
        )

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
        assertTrue(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS)!!.contains(API_KEY_HEADER))
    }

    private companion object {
        const val API_KEY_HEADER = "X-API-Key"
        const val TEST_API_KEY = "test-secret"
    }
}
