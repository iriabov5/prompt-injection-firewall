package com.ryabov.promptfirewall.ai

import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher

/**
 * Micronaut HTTP client для OpenAI-compatible `/chat/completions`.
 */
@Client("\${ai.base-url}")
@Requires(property = "ai.enabled", value = "true")
interface OpenAiChatCompletionsClient {

    /**
     * Отправляет chat completion request с bearer-токеном provider.
     */
    @Post("/chat/completions")
    fun complete(
        @Header("Authorization") authorization: String,
        @Body request: OpenAiChatCompletionRequest
    ): Publisher<OpenAiChatCompletionResponse>
}
