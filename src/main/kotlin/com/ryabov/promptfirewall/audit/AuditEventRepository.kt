package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.Decision
import io.micronaut.context.annotation.Requires
import io.micronaut.data.model.Pageable
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

/**
 * Micronaut Data JDBC repository для PostgreSQL-backed audit events.
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
@Requires(property = "audit.enabled", value = "true")
interface AuditEventRepository : PageableRepository<PersistentAuditEvent, Long> {

    fun findAllOrderByCreatedAtDesc(pageable: Pageable): List<PersistentAuditEvent>

    fun countByDecision(decision: Decision): Long
}
