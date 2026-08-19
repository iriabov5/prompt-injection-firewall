package com.ryabov.promptfirewall.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("Swagger UI")
class SwaggerUiIntegrationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    @DisplayName("Публикует страницу Swagger UI через Micronaut static resources")
    fun `serves swagger ui page`() {
        val html = client.toBlocking().retrieve(HttpRequest.GET<Any>("/swagger-ui/index.html"))

        assertTrue(html.contains("SwaggerUIBundle"))
        assertTrue(html.contains("prompt-injection-firewall-api-0.1.0"))
    }

    @Test
    @DisplayName("Swagger UI ссылается на generated OpenAPI specification")
    fun `swagger ui points to generated openapi specification`() {
        val html = client.toBlocking().retrieve(HttpRequest.GET<Any>("/swagger-ui/index.html"))

        assertTrue(html.contains("/swagger/prompt-injection-firewall-api-0.1.0.yml"))
    }

    @Test
    @DisplayName("Публикует generated OpenAPI specification рядом со Swagger UI")
    fun `serves generated openapi specification`() {
        val specification = client.toBlocking().retrieve(
            HttpRequest.GET<Any>("/swagger/prompt-injection-firewall-api-0.1.0.yml")
        )

        assertTrue(specification.contains("/api/v1/prompts/analyze:"))
        assertTrue(specification.contains("PromptAnalyzeResponse:"))
    }
}
