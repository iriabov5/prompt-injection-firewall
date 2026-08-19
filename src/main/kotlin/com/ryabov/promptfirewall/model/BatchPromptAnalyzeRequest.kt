package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

/**
 * HTTP/body model для batch-анализа prompt-запросов.
 */
@Serdeable
@Schema(description = "Запрос batch-анализа prompts")
data class BatchPromptAnalyzeRequest(
    /** Непустой список prompt-запросов, обрабатываемых с сохранением порядка. */
    @field:NotEmpty
    @field:Size(max = 100)
    @field:Valid
    @field:Schema(description = "Список prompt-запросов для анализа")
    val items: List<PromptAnalyzeRequest>
)
