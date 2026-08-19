package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Итоговое действие firewall, которое вызывающая система может применить к prompt.
 */
@Serdeable
@Schema(description = "Итоговое действие firewall")
enum class Decision {
    /** Prompt можно пропустить без дополнительной проверки. */
    ALLOW,
    /** Prompt стоит отправить на ручную или дополнительную проверку. */
    REVIEW,
    /** Prompt нужно заблокировать как высокорисковый. */
    BLOCK
}
