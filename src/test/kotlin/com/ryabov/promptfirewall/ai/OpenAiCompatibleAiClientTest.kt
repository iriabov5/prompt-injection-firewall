package com.ryabov.promptfirewall.ai

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

@DisplayName("OpenAI-compatible AI client")
class OpenAiCompatibleAiClientTest {

    private val httpClient = mockk<OpenAiChatCompletionsClient>()
    private val properties = AiProperties().apply {
        apiKey = "test-key"
        model = "test-model"
        timeoutMs = 100
    }
    private val aiClient = OpenAiCompatibleAiClient(httpClient, properties)

    @Test
    @DisplayName("Парсит успешный ответ provider в AI analysis result")
    fun `parses successful provider response`() {
        every { httpClient.complete(any(), any()) } returns Mono.just(
            OpenAiChatCompletionResponse(
                choices = listOf(
                    OpenAiChatChoice(
                        OpenAiChatMessage(
                            role = "assistant",
                            content = "65|model_detected_injection|AI нашел риск"
                        )
                    )
                )
            )
        )

        val result = aiClient.analyze("bad prompt").join()

        assertEquals(65, result?.score)
        assertEquals("model_detected_injection", result?.reason)
        assertEquals("AI нашел риск", result?.summary)
    }

    @Test
    @DisplayName("Возвращает null для некорректного ответа provider")
    fun `returns null for malformed provider response`() {
        every { httpClient.complete(any(), any()) } returns Mono.just(
            OpenAiChatCompletionResponse(
                choices = listOf(
                    OpenAiChatChoice(
                        OpenAiChatMessage(
                            role = "assistant",
                            content = "not-a-score"
                        )
                    )
                )
            )
        )

        val result = aiClient.analyze("bad prompt").join()

        assertNull(result)
    }

    @Test
    @DisplayName("Возвращает null при ошибке provider")
    fun `returns null when provider fails`() {
        every { httpClient.complete(any(), any()) } returns Mono.error(IllegalStateException("provider failed"))

        val result = aiClient.analyze("bad prompt").join()

        assertNull(result)
    }
}
