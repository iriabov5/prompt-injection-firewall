package com.ryabov.promptfirewall.ai

import java.util.concurrent.CompletableFuture

/**
 * Порт AI provider, скрывающий конкретный OpenAI-compatible transport от analyzer layer.
 */
fun interface AiClient {

    /**
     * Возвращает AI-assisted оценку prompt или `null`, если provider не смог дать пригодный результат.
     */
    fun analyze(prompt: String): CompletableFuture<AiAnalysisResult?>
}
