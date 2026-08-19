package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH
}
