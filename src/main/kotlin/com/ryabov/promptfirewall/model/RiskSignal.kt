package com.ryabov.promptfirewall.model

data class RiskSignal(
    val code: String,
    val weight: Int,
    val description: String
)
