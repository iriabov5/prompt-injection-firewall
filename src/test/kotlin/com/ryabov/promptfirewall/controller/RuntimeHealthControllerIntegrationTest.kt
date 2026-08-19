package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.model.RuntimeHealthResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("HTTP health endpoint runtime")
class RuntimeHealthControllerIntegrationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    @DisplayName("Возвращает состояние runtime, число анализаторов и выключенный AI provider")
    fun `reports heuristic runtime health`() {
        val response = client.toBlocking().retrieve(
            HttpRequest.GET<Any>("/api/v1/health"),
            RuntimeHealthResponse::class.java
        )

        assertEquals("UP", response.status)
        assertEquals(5, response.analyzerCount)
        assertEquals("disabled", response.aiProvider)
    }
}
