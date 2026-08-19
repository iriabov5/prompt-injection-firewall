package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * PostgreSQL-backed audit recorder. Запись выполняется асинхронно и best-effort,
 * поэтому временная ошибка БД не меняет ответ prompt analysis клиенту.
 */
@Singleton
@Requires(property = "audit.enabled", value = "true")
class JdbcAuditRecorder(
    private val auditEventRepository: AuditEventRepository,
    private val auditEventMapper: AuditEventMapper
) : AuditRecorder {
    private val executor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()

    override fun record(request: PromptAnalyzeRequest, response: PromptAnalyzeResponse) {
        CompletableFuture
            .runAsync(
                {
                    auditEventRepository.save(auditEventMapper.toEntity(request, response))
                },
                executor
            )
            .exceptionally { exception ->
                logger.warn("Audit event persistence failed: {}", exception.message)
                null
            }
    }

    private companion object {
        val logger = LoggerFactory.getLogger(JdbcAuditRecorder::class.java)
    }
}
