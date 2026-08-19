package com.ryabov.promptfirewall.model

data class PromptAnalyzeResponse(
    val risk: RiskLevel,
    val score: Int,
    val decision: Decision,
    val reasons: List<String>,
    val signals: List<RiskSignal>,
    val aiSummary: String? = null,
    val latencyMs: Long
)
