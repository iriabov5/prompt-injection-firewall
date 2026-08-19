package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * HTTP/body model для анализа одного prompt с необязательным контекстом источника.
 */
@Serdeable
data class PromptAnalyzeRequest(
    /** Текст prompt, который должен быть проверен firewall. */
    @field:NotBlank
    @field:Size(max = 12000)
    val prompt: String,
    /** Дополнительный контекст вызова, если он нужен клиенту для трассировки. */
    @field:Size(max = 12000)
    val context: String? = null,
    /** Короткая метка источника prompt, например имя интеграции или канала. */
    @field:Size(max = 64)
    val source: String? = null
)
