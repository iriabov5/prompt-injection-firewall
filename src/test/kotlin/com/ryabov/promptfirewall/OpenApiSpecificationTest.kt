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
        assertTrue(specification.contains("/api/v1/rules:"))
        assertTrue(specification.contains("/api/v1/rules/{id}:"))
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
        assertTrue(specification.contains("CustomRuleCreateRequest:"))
        assertTrue(specification.contains("CustomRuleResponse:"))
        assertTrue(specification.contains("CustomRuleType:"))
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

    @Test
    @DisplayName("Описывает API key security scheme для защищенных endpoints")
    fun `generated specification contains api key security scheme`() {
        val specification = readSpecification()

        assertTrue(specification.contains("ApiKeyAuth:"))
        assertTrue(specification.contains("type: apiKey"))
        assertTrue(specification.contains("name: X-API-Key"))
        assertTrue(specification.contains("security:"))
    }

    @Test
    @DisplayName("Требует API key для analysis operations и не требует его для health")
    fun `generated specification marks protected and anonymous operations`() {
        val specification = readSpecification()

        assertTrue(operationBlock(specification, "/api/v1/prompts/analyze:").contains("ApiKeyAuth: []"))
        assertTrue(operationBlock(specification, "/api/v1/prompts/analyze/batch:").contains("ApiKeyAuth: []"))
        assertTrue(operationBlock(specification, "/api/v1/rules:").contains("ApiKeyAuth: []"))
        assertTrue(operationBlock(specification, "/api/v1/rules/{id}:").contains("ApiKeyAuth: []"))
        assertTrue(!operationBlock(specification, "/api/v1/health:").contains("ApiKeyAuth: []"))
    }

    private fun readSpecification(): String {
        val resource = javaClass.classLoader.getResourceAsStream(SPECIFICATION_RESOURCE)

        assertNotNull(resource, "OpenAPI specification resource must be generated")

        return resource!!.bufferedReader().use { it.readText() }
    }

    private fun operationBlock(specification: String, path: String): String {
        val start = specification.indexOf("  $path")
        val next = specification.indexOf("\n  /", start + path.length)

        return if (next == -1) {
            specification.substring(start)
        } else {
            specification.substring(start, next)
        }
    }

    private companion object {
        const val SPECIFICATION_RESOURCE = "META-INF/swagger/prompt-injection-firewall-api-0.1.0.yml"
    }
}
