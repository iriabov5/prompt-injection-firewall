package com.ryabov.promptfirewall.analyzer

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.RiskSignal
import java.util.concurrent.CompletableFuture

/**
 * Контракт эвристического анализатора, который ищет в prompt один тип риска и
 * возвращает найденные сигналы асинхронно.
 */
fun interface PromptRiskAnalyzer {

    /**
     * Анализирует входной prompt и завершает future списком найденных risk signals.
     * Пустой список означает, что анализатор не нашел своего признака риска.
     */
    fun analyze(request: PromptAnalyzeRequest): CompletableFuture<List<RiskSignal>>
}
