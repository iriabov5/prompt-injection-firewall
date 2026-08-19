package com.ryabov.promptfirewall.service

import com.ryabov.promptfirewall.audit.AuditRecorder
import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Оркестрирует параллельный запуск анализаторов prompt через [CompletableFuture]
 * и собирает их результаты в единое firewall-решение.
 */
class PromptFirewallService(
    private val analyzers: List<PromptRiskAnalyzer>,
    private val riskAggregator: RiskAggregator = RiskAggregator(),
    private val analyzerTimeout: Duration = DEFAULT_ANALYZER_TIMEOUT,
    private val executor: Executor = DEFAULT_EXECUTOR,
    private val promptAnalysisMetrics: PromptAnalysisMetrics? = null,
    private val auditRecorder: AuditRecorder? = null
) {

    /**
     * Запускает все анализаторы конкурентно, применяет timeout/fallback к каждому
     * из них и возвращает aggregate response без падения из-за одного сбойного анализатора.
     */
    fun analyze(request: PromptAnalyzeRequest): CompletableFuture<PromptAnalyzeResponse> {
        val startedAt = System.nanoTime()
        val futures = analyzers.map { analyzer ->
            CompletableFuture
                .supplyAsync({ analyzer.analyze(request) }, executor)
                .thenCompose { it }
                .completeOnTimeout(emptyList(), analyzerTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .exceptionally { emptyList() }
        }

        return CompletableFuture
            .allOf(*futures.toTypedArray())
            .thenApply {
                val signals = futures.flatMap { it.join() }
                riskAggregator.aggregate(signals, elapsedMillis(startedAt))
                    .also { response ->
                        promptAnalysisMetrics?.record(request, response)
                        auditRecorder?.record(request, response)
                    }
            }
    }

    /**
     * Анализирует batch prompt-запросов и сохраняет порядок ответов таким же,
     * как порядок входных элементов.
     */
    fun analyzeBatch(requests: List<PromptAnalyzeRequest>): CompletableFuture<List<PromptAnalyzeResponse>> {
        val futures = requests.map { analyze(it) }

        return CompletableFuture
            .allOf(*futures.toTypedArray())
            .thenApply { futures.map { it.join() } }
    }

    private fun elapsedMillis(startedAt: Long): Long =
        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)

    private companion object {
        val DEFAULT_ANALYZER_TIMEOUT: Duration = Duration.ofMillis(500)
        val DEFAULT_EXECUTOR: Executor = Executors.newVirtualThreadPerTaskExecutor()
    }
}
