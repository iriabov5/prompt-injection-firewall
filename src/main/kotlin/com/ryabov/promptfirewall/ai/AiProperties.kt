package com.ryabov.promptfirewall.ai

import io.micronaut.context.annotation.ConfigurationProperties

/**
 * Настройки опционального AI provider, совместимого с OpenAI chat completions API.
 */
@ConfigurationProperties("ai")
class AiProperties {
    /** Включает создание AI analyzer и HTTP client bean. */
    var enabled: Boolean = false

    /** Базовый URL provider, например `https://api.openai.com/v1`. */
    var baseUrl: String = "https://api.openai.com/v1"

    /** API key provider; используется только при `enabled=true`. */
    var apiKey: String? = null

    /** Имя модели OpenAI-compatible provider. */
    var model: String = "gpt-4o-mini"

    /** Timeout AI-запроса в миллисекундах, после которого analyzer возвращает fallback. */
    var timeoutMs: Long = 1_000
}
