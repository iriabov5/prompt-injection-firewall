package com.ryabov.promptfirewall.ai

import java.util.concurrent.CompletableFuture

/**
 * Тестовый [AiClient], который позволяет управлять ответом provider без сетевых вызовов.
 */
class FakeAiClient(
    private val result: CompletableFuture<AiAnalysisResult?>
) : AiClient {

    override fun analyze(prompt: String): CompletableFuture<AiAnalysisResult?> = result
}
