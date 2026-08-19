package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable

/**
 * Итоговое действие firewall, которое вызывающая система может применить к prompt.
 */
@Serdeable
enum class Decision {
    /** Prompt можно пропустить без дополнительной проверки. */
    ALLOW,
    /** Prompt стоит отправить на ручную или дополнительную проверку. */
    REVIEW,
    /** Prompt нужно заблокировать как высокорисковый. */
    BLOCK
}
