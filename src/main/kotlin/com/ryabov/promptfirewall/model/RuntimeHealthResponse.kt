package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Диагностический ответ health endpoint с кратким состоянием runtime.
 */
@Serdeable
@Schema(description = "Состояние runtime сервиса")
data class RuntimeHealthResponse(
    /** Текущее состояние HTTP runtime. */
    @field:Schema(description = "Статус сервиса", example = "UP")
    val status: String,
    /** Количество analyzer beans, подключенных к firewall service. */
    @field:Schema(description = "Количество подключенных анализаторов", example = "5")
    val analyzerCount: Int,
    /** Состояние AI provider в текущей конфигурации. */
    @field:Schema(description = "Состояние AI provider", example = "disabled")
    val aiProvider: String
)
