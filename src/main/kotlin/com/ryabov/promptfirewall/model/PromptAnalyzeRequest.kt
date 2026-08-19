package com.ryabov.promptfirewall.model

data class PromptAnalyzeRequest(
    val prompt: String,
    val context: String? = null,
    val source: String? = null
)
