package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.AuditEventResponse
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import jakarta.inject.Singleton
import java.time.Instant

/**
 * Преобразует analysis request/response в persistent audit event и обратно,
 * не добавляя в audit model исходный prompt.
 */
@Singleton
class AuditEventMapper(
    private val promptHashService: PromptHashService
) {

    fun toEntity(request: PromptAnalyzeRequest, response: PromptAnalyzeResponse): PersistentAuditEvent =
        PersistentAuditEvent(
            promptHash = promptHashService.hash(request.prompt),
            source = request.source,
            score = response.score,
            risk = response.risk,
            decision = response.decision,
            reasons = response.reasons.joinToString(REASONS_SEPARATOR),
            latencyMs = response.latencyMs,
            aiUsed = response.reasons.contains(AI_ASSISTED_REASON)
        )

    fun toResponse(event: PersistentAuditEvent): AuditEventResponse =
        AuditEventResponse(
            id = event.id ?: 0,
            promptHash = event.promptHash,
            source = event.source,
            score = event.score,
            risk = event.risk,
            decision = event.decision,
            reasons = event.reasons
                .split(REASONS_SEPARATOR)
                .filter(String::isNotBlank),
            latencyMs = event.latencyMs,
            aiUsed = event.aiUsed,
            createdAt = event.createdAt ?: Instant.EPOCH
        )

    private companion object {
        const val AI_ASSISTED_REASON = "ai_assisted_risk"
        const val REASONS_SEPARATOR = ","
    }
}
