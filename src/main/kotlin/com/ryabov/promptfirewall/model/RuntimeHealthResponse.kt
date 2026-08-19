package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class RuntimeHealthResponse(
    val status: String,
    val analyzerCount: Int,
    val aiProvider: String
)
