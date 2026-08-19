package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Ответ batch-анализа, где порядок результатов соответствует порядку входных items.
 */
@Serdeable
@Schema(description = "Ответ batch-анализа prompts")
data class BatchPromptAnalyzeResponse(
    /** Результаты анализа для каждого входного prompt-запроса. */
    @field:Schema(description = "Результаты анализа в порядке входных items")
    val results: List<PromptAnalyzeResponse>
)
