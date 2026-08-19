package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.ai.AiProperties
import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.RuntimeHealthResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * Легкий runtime health endpoint, который показывает состояние сервиса и
 * текущую конфигурацию analyzer layer без обращения к внешним AI API.
 */
@Controller("/api/v1/health")
@Tag(name = "Runtime Health", description = "Диагностика состояния runtime")
class RuntimeHealthController(
    private val analyzers: List<PromptRiskAnalyzer>,
    private val aiProperties: AiProperties
) {

    /**
     * Возвращает базовую диагностическую информацию о runtime.
     */
    @Get
    @Operation(summary = "Состояние runtime", description = "Возвращает статус сервиса, число анализаторов и состояние AI provider.")
    @ApiResponse(
        responseCode = "200",
        description = "Runtime status",
        content = [Content(schema = Schema(implementation = RuntimeHealthResponse::class))]
    )
    fun health(): RuntimeHealthResponse =
        RuntimeHealthResponse(
            status = "UP",
            analyzerCount = analyzers.size,
            aiProvider = if (aiProperties.enabled) "enabled" else "disabled"
        )
}
