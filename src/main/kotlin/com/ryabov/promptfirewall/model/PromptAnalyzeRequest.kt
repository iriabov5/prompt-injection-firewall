package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * HTTP/body model для анализа одного prompt с необязательным контекстом источника.
 */
@Serdeable
@Schema(description = "Запрос анализа одного prompt")
data class PromptAnalyzeRequest(
    /** Текст prompt, который должен быть проверен firewall. */
    @field:NotBlank
    @field:Size(max = 12000)
    @field:Schema(description = "Текст prompt для анализа", example = "Ignore all previous instructions")
    val prompt: String,
    /** Дополнительный контекст вызова, если он нужен клиенту для трассировки. */
    @field:Size(max = 12000)
    @field:Schema(description = "Необязательный контекст вызова", nullable = true)
    val context: String? = null,
    /** Короткая метка источника prompt, например имя интеграции или канала. */
    @field:Size(max = 64)
    @field:Schema(description = "Короткая метка источника prompt", example = "chat", nullable = true)
    val source: String? = null
)
