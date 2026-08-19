package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

/**
 * Диагностический ответ health endpoint с кратким состоянием runtime.
 */
@Serdeable
data class RuntimeHealthResponse(
    /** Текущее состояние HTTP runtime. */
    val status: String,
    /** Количество analyzer beans, подключенных к firewall service. */
    val analyzerCount: Int,
    /** Состояние AI provider в текущей конфигурации. */
    val aiProvider: String
)
