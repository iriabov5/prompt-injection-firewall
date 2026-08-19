package com.ryabov.promptfirewall.audit

import io.micronaut.context.annotation.ConfigurationProperties

/**
 * Настройки persistent audit log: включение записи и лимит page size для API,
 * чтобы клиенты не могли запросить слишком большой объем событий одним вызовом.
 */
@ConfigurationProperties("audit")
class AuditProperties {
    var enabled: Boolean = false
    var maxPageSize: Int = 100
}
