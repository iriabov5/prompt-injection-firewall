package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.audit.AuditQueryService
import com.ryabov.promptfirewall.model.AuditEventResponse
import com.ryabov.promptfirewall.model.AuditStatsResponse
import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import reactor.core.publisher.Mono

/**
 * Protected reactive HTTP API для просмотра audit-событий и агрегированной
 * статистики по решениям Prompt Injection Firewall.
 */
@Controller("/api/v1/audit")
@Requires(property = "audit.enabled", value = "true")
@Tag(name = "Audit Log", description = "Просмотр audit trail результатов анализа prompt")
@SecurityRequirement(name = "ApiKeyAuth")
open class AuditController(
    private val auditQueryService: AuditQueryService
) {

    /**
     * Возвращает последние audit events в порядке от новых к старым.
     */
    @Get("/events")
    @Operation(summary = "Последние audit events", description = "Возвращает последние audit events без исходного prompt text.")
    @ApiResponse(
        responseCode = "200",
        description = "Audit events получены",
        content = [Content(array = ArraySchema(schema = Schema(implementation = AuditEventResponse::class)))]
    )
    open fun latest(@QueryValue(defaultValue = "50") limit: Int?): Mono<List<AuditEventResponse>> =
        Mono.fromSupplier { auditQueryService.latest(limit) }

    /**
     * Возвращает aggregate statistics по firewall decisions.
     */
    @Get("/stats")
    @Operation(summary = "Статистика audit log", description = "Возвращает counts по ALLOW, REVIEW и BLOCK decisions.")
    @ApiResponse(
        responseCode = "200",
        description = "Audit statistics получена",
        content = [Content(schema = Schema(implementation = AuditStatsResponse::class))]
    )
    open fun stats(): Mono<AuditStatsResponse> =
        Mono.fromSupplier(auditQueryService::stats)
}
