package com.ryabov.promptfirewall.model

data class BatchPromptAnalyzeResponse(
    val results: List<PromptAnalyzeResponse>
)
