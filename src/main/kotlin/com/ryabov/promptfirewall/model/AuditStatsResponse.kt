package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Aggregate statistics по audit log, сгруппированная по firewall decision.
 */
@Serdeable
@Schema(description = "Статистика audit log по решениям firewall")
data class AuditStatsResponse(
    /** Общее количество audit events. */
    @field:Schema(description = "Общее количество audit events")
    val total: Long,
    /** Количество ALLOW decisions. */
    @field:Schema(description = "Количество ALLOW decisions")
    val allow: Long,
    /** Количество REVIEW decisions. */
    @field:Schema(description = "Количество REVIEW decisions")
    val review: Long,
    /** Количество BLOCK decisions. */
    @field:Schema(description = "Количество BLOCK decisions")
    val block: Long
)
