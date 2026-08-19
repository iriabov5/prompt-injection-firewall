package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Serdeable
data class PromptAnalyzeRequest(
    @field:NotBlank
    @field:Size(max = 12000)
    val prompt: String,
    @field:Size(max = 12000)
    val context: String? = null,
    @field:Size(max = 64)
    val source: String? = null
)
