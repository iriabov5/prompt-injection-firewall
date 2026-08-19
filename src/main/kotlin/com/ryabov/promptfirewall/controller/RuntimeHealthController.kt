package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.RuntimeHealthResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api/v1/health")
class RuntimeHealthController(
    private val analyzers: List<PromptRiskAnalyzer>
) {

    @Get
    fun health(): RuntimeHealthResponse =
        RuntimeHealthResponse(
            status = "UP",
            analyzerCount = analyzers.size,
            aiProvider = "disabled"
        )
}
