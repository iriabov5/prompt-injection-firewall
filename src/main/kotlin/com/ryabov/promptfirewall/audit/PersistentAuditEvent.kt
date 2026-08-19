package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.Decision
import com.ryabov.promptfirewall.model.RiskLevel
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.Instant

/**
 * PostgreSQL entity audit-события. Полный prompt намеренно отсутствует:
 * для расследований хранится только deterministic SHA-256 hash.
 */
@MappedEntity("prompt_analysis_audit_events")
data class PersistentAuditEvent(
    @field:Id
    @field:GeneratedValue
    val id: Long? = null,
    val promptHash: String,
    val source: String?,
    val score: Int,
    val risk: RiskLevel,
    val decision: Decision,
    val reasons: String,
    val latencyMs: Long,
    val aiUsed: Boolean,
    @field:DateCreated
    val createdAt: Instant? = null
)
