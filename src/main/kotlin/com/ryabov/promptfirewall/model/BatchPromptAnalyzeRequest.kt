package com.ryabov.promptfirewall.model

data class BatchPromptAnalyzeRequest(
    val items: List<PromptAnalyzeRequest>
)
