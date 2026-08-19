package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.model.BatchPromptAnalyzeRequest
import com.ryabov.promptfirewall.model.BatchPromptAnalyzeResponse
import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse
import com.ryabov.promptfirewall.service.PromptFirewallService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.hateoas.JsonError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import reactor.core.publisher.Mono

/**
 * HTTP boundary для анализа prompt: принимает одиночные и batch-запросы,
 * а внутренние CompletableFuture возвращает наружу как Reactor Mono.
 */
@Controller("/api/v1/prompts")
@Tag(name = "Prompt Analysis", description = "Анализ prompts на признаки prompt injection")
open class PromptAnalyzeController(
    private val promptFirewallService: PromptFirewallService
) {

    /**
     * Анализирует один prompt и возвращает risk score, decision и объяснимые reasons.
     */
    @Post("/analyze")
    @Operation(summary = "Анализ одного prompt", description = "Возвращает risk score, decision и объяснимые risk signals.")
    @ApiResponse(
        responseCode = "200",
        description = "Prompt успешно проанализирован",
        content = [Content(schema = Schema(implementation = PromptAnalyzeResponse::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Ошибка validation request body",
        content = [Content(schema = Schema(implementation = JsonError::class))]
    )
    open fun analyze(@Body @Valid request: PromptAnalyzeRequest): Mono<PromptAnalyzeResponse> =
        Mono.fromFuture(promptFirewallService.analyze(request))

    /**
     * Анализирует список prompt-запросов и возвращает результаты в исходном порядке.
     */
    @Post("/analyze/batch")
    @Operation(summary = "Batch-анализ prompts", description = "Возвращает результаты анализа в том же порядке, что и входные items.")
    @ApiResponse(
        responseCode = "200",
        description = "Batch успешно проанализирован",
        content = [Content(schema = Schema(implementation = BatchPromptAnalyzeResponse::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Ошибка validation request body",
        content = [Content(schema = Schema(implementation = JsonError::class))]
    )
    open fun analyzeBatch(@Body @Valid request: BatchPromptAnalyzeRequest): Mono<BatchPromptAnalyzeResponse> =
        Mono.fromFuture(
            promptFirewallService
                .analyzeBatch(request.items)
                .thenApply(::BatchPromptAnalyzeResponse)
        )
}
