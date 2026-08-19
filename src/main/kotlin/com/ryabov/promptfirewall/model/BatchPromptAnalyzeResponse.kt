package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class BatchPromptAnalyzeResponse(
    val results: List<PromptAnalyzeResponse>
)
