package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

@Serdeable
data class BatchPromptAnalyzeRequest(
    @field:NotEmpty
    @field:Size(max = 100)
    @field:Valid
    val items: List<PromptAnalyzeRequest>
)
