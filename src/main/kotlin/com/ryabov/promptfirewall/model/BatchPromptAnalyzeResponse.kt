package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

/**
 * Ответ batch-анализа, где порядок результатов соответствует порядку входных items.
 */
@Serdeable
data class BatchPromptAnalyzeResponse(
    /** Результаты анализа для каждого входного prompt-запроса. */
    val results: List<PromptAnalyzeResponse>
)
