package com.ryabov.promptfirewall.ai

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@Property(name = "ai.enabled", value = "true")
@Property(name = "ai.base-url", value = "http://localhost:65535")
@Property(name = "ai.api-key", value = "test-key")
@Property(name = "ai.model", value = "test-model")
@DisplayName("Micronaut conditional beans для включенного AI provider")
class AiConditionalBeanIntegrationTest {

    @Inject
    lateinit var beanContext: BeanContext

    @Inject
    lateinit var analyzers: List<PromptRiskAnalyzer>

    @Inject
    lateinit var aiProperties: AiProperties

    @Test
    @DisplayName("Создает AI analyzer и OpenAI-compatible client только при ai.enabled=true")
    fun `creates ai beans when ai is enabled`() {
        assertTrue(beanContext.containsBean(AiPromptAnalyzer::class.java))
        assertTrue(beanContext.containsBean(AiClient::class.java))
        assertEquals(6, analyzers.size)
    }

    @Test
    @DisplayName("Биндит настройки AI provider из Micronaut configuration")
    fun `binds ai provider properties`() {
        assertTrue(aiProperties.enabled)
        assertEquals("http://localhost:65535", aiProperties.baseUrl)
        assertEquals("test-key", aiProperties.apiKey)
        assertEquals("test-model", aiProperties.model)
    }
}
