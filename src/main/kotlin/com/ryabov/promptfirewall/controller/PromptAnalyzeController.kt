package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.model.BatchPromptAnalyzeRequest
import com.ryabov.promptfirewall.model.BatchPromptAnalyzeResponse
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import com.ryabov.promptfirewall.service.PromptFirewallService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.validation.Valid
import reactor.core.publisher.Mono

/**
 * HTTP boundary для анализа prompt: принимает одиночные и batch-запросы,
 * а внутренние CompletableFuture возвращает наружу как Reactor Mono.
 */
@Controller("/api/v1/prompts")
open class PromptAnalyzeController(
    private val promptFirewallService: PromptFirewallService
) {

    /**
     * Анализирует один prompt и возвращает risk score, decision и объяснимые reasons.
     */
    @Post("/analyze")
    open fun analyze(@Body @Valid request: PromptAnalyzeRequest): Mono<PromptAnalyzeResponse> =
        Mono.fromFuture(promptFirewallService.analyze(request))

    /**
     * Анализирует список prompt-запросов и возвращает результаты в исходном порядке.
     */
    @Post("/analyze/batch")
    open fun analyzeBatch(@Body @Valid request: BatchPromptAnalyzeRequest): Mono<BatchPromptAnalyzeResponse> =
        Mono.fromFuture(
            promptFirewallService
                .analyzeBatch(request.items)
                .thenApply(::BatchPromptAnalyzeResponse)
        )
}
