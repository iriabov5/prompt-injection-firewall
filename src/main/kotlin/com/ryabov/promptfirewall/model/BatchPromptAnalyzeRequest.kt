package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

/**
 * HTTP/body model для batch-анализа prompt-запросов.
 */
@Serdeable
data class BatchPromptAnalyzeRequest(
    /** Непустой список prompt-запросов, обрабатываемых с сохранением порядка. */
    @field:NotEmpty
    @field:Size(max = 100)
    @field:Valid
    val items: List<PromptAnalyzeRequest>
)
