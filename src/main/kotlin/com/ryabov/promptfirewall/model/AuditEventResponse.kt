package com.ryabov.promptfirewall.model

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * API response audit-события без исходного prompt text.
 */
@Serdeable
@Schema(description = "Audit event результата анализа prompt")
data class AuditEventResponse(
    /** Identifier audit event в PostgreSQL. */
    @field:Schema(description = "Identifier audit event")
    val id: Long,
    /** SHA-256 hash исходного prompt с префиксом алгоритма. */
    @field:Schema(description = "Deterministic prompt hash", example = "sha256:...")
    val promptHash: String,
    /** Короткая метка источника prompt. */
    @field:Schema(description = "Источник prompt", nullable = true)
    val source: String?,
    /** Итоговый score анализа. */
    @field:Schema(description = "Итоговый risk score", example = "75")
    val score: Int,
    /** Итоговый risk level. */
    @field:Schema(description = "Risk level")
    val risk: RiskLevel,
    /** Итоговое firewall decision. */
    @field:Schema(description = "Firewall decision")
    val decision: Decision,
    /** Reasons, которые участвовали в итоговом решении. */
    @field:Schema(description = "Reasons итогового решения")
    val reasons: List<String>,
    /** Latency анализа prompt в миллисекундах. */
    @field:Schema(description = "Latency анализа в миллисекундах")
    val latencyMs: Long,
    /** Был ли в результате учтен AI-assisted signal. */
    @field:Schema(description = "Признак использования AI-assisted signal")
    val aiUsed: Boolean,
    /** Время создания audit event. */
    @field:Schema(description = "Время создания audit event")
    val createdAt: Instant
)
