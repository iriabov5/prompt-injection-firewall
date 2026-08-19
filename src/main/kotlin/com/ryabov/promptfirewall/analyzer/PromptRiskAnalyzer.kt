package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import java.util.concurrent.CompletableFuture

interface PromptRiskAnalyzer {

    fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>>
}
