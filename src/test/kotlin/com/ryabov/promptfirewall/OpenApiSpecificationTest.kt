package com.ryabov.promptfirewall

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("OpenAPI specification")
class OpenApiSpecificationTest {

    @Test
    @DisplayName("Генерируется и содержит все public HTTP endpoints")
    fun `generated specification contains public endpoints`() {
        val specification = readSpecification()

        assertTrue(specification.contains("/api/v1/prompts/analyze:"))
        assertTrue(specification.contains("/api/v1/prompts/analyze/batch:"))
        assertTrue(specification.contains("/api/v1/health:"))
    }

    @Test
    @DisplayName("Описывает request и response schemas public API")
    fun `generated specification contains request and response schemas`() {
        val specification = readSpecification()

        assertTrue(specification.contains("PromptAnalyzeRequest:"))
        assertTrue(specification.contains("PromptAnalyzeResponse:"))
        assertTrue(specification.contains("BatchPromptAnalyzeRequest:"))
        assertTrue(specification.contains("BatchPromptAnalyzeResponse:"))
        assertTrue(specification.contains("RuntimeHealthResponse:"))
        assertTrue(specification.contains("RiskSignal:"))
    }

    @Test
    @DisplayName("Описывает validation constraints и error responses")
    fun `generated specification contains validation constraints and error responses`() {
        val specification = readSpecification()

        assertTrue(specification.contains("maxLength: 12000"))
        assertTrue(specification.contains("maxLength: 64"))
        assertTrue(specification.contains("minItems: 1"))
        assertTrue(specification.contains("\"400\":"))
        assertTrue(specification.contains("Ошибка validation request body"))
        assertTrue(specification.contains("JsonError:"))
    }

    private fun readSpecification(): String {
        val resource = javaClass.classLoader.getResourceAsStream(SPECIFICATION_RESOURCE)

        assertNotNull(resource, "OpenAPI specification resource must be generated")

        return resource!!.bufferedReader().use { it.readText() }
    }

    private companion object {
        const val SPECIFICATION_RESOURCE = "META-INF/swagger/prompt-injection-firewall-api-0.1.0.yml"
    }
}
