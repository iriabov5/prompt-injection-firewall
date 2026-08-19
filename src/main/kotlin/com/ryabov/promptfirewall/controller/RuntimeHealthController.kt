package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.RuntimeHealthResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

/**
 * Легкий runtime health endpoint, который показывает состояние сервиса и
 * текущую конфигурацию analyzer layer без обращения к внешним AI API.
 */
@Controller("/api/v1/health")
class RuntimeHealthController(
    private val analyzers: List<PromptRiskAnalyzer>
) {

    /**
     * Возвращает базовую диагностическую информацию о runtime.
     */
    @Get
    fun health(): RuntimeHealthResponse =
        RuntimeHealthResponse(
            status = "UP",
            analyzerCount = analyzers.size,
            aiProvider = "disabled"
        )
}
