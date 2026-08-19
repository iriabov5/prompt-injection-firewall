package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton

/**
 * No-op audit recorder для режима без подключенного PostgreSQL audit log.
 */
@Singleton
@Requires(property = "audit.enabled", notEquals = "true")
class NoopAuditRecorder : AuditRecorder {
    override fun record(request: PromptAnalyzeRequest, response: PromptAnalyzeResponse) = Unit
}
