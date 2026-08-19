package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class RiskSignal(
    val code: String,
    val weight: Int,
    val description: String
)
