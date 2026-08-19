package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
enum class Decision {
    ALLOW,
    REVIEW,
    BLOCK
}
