package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.AuditEventResponse
import com.ryabov.promptfirewall.model.AuditStatsResponse
import com.ryabov.promptfirewall.model.Decision
import io.micronaut.context.annotation.Requires
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

/**
 * Query service для protected audit API: применяет page-size лимиты и собирает
 * простую статистику по решениям firewall.
 */
@Singleton
@Requires(property = "audit.enabled", value = "true")
class AuditQueryService(
    private val auditEventRepository: AuditEventRepository,
    private val auditProperties: AuditProperties,
    private val auditEventMapper: AuditEventMapper
) {

    fun latest(limit: Int?): List<AuditEventResponse> =
        auditEventRepository
            .findAllOrderByCreatedAtDesc(Pageable.from(0, normalizedLimit(limit)))
            .map(auditEventMapper::toResponse)

    fun stats(): AuditStatsResponse =
        AuditStatsResponse(
            total = auditEventRepository.count(),
            allow = auditEventRepository.countByDecision(Decision.ALLOW),
            review = auditEventRepository.countByDecision(Decision.REVIEW),
            block = auditEventRepository.countByDecision(Decision.BLOCK)
        )

    private fun normalizedLimit(limit: Int?): Int =
        limit
            ?.coerceIn(MIN_PAGE_SIZE, auditProperties.maxPageSize)
            ?: auditProperties.maxPageSize

    private companion object {
        const val MIN_PAGE_SIZE = 1
    }
}
