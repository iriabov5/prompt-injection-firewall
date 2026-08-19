package com.ryabov.promptfirewall.ai

import io.micronaut.serde.annotation.Serdeable

/**
 * Минимальная request model для OpenAI-compatible chat completions API.
 */
@Serdeable
data class OpenAiChatCompletionRequest(
    val model: String,
    val messages: List<OpenAiChatMessage>,
    val temperature: Double = 0.0
)

/**
 * Сообщение chat completions API с ролью и текстовым содержимым.
 */
@Serdeable
data class OpenAiChatMessage(
    val role: String,
    val content: String
)

/**
 * Минимальная response model chat completions API, достаточная для чтения первого ответа модели.
 */
@Serdeable
data class OpenAiChatCompletionResponse(
    val choices: List<OpenAiChatChoice> = emptyList()
)

/**
 * Один вариант ответа OpenAI-compatible provider.
 */
@Serdeable
data class OpenAiChatChoice(
    val message: OpenAiChatMessage
)
