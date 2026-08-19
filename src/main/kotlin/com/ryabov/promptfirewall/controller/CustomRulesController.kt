package com.ryabov.promptfirewall.controller

import com.ryabov.promptfirewall.customrules.CustomRuleRegistry
import com.ryabov.promptfirewall.customrules.toResponse
import com.ryabov.promptfirewall.model.CustomRuleCreateRequest
import com.ryabov.promptfirewall.model.CustomRuleResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.hateoas.JsonError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import reactor.core.publisher.Mono

/**
 * Protected HTTP API для управления in-memory custom rules, которые расширяют
 * prompt analysis без пересборки приложения.
 */
@Controller("/api/v1/rules")
@Tag(name = "Custom Rules", description = "Управление пользовательскими правилами анализа prompt")
@SecurityRequirement(name = "ApiKeyAuth")
open class CustomRulesController(
    private val customRuleRegistry: CustomRuleRegistry
) {

    /**
     * Создает custom rule и возвращает сохраненное представление с generated identifier.
     */
    @Post
    @Operation(summary = "Создать custom rule", description = "Добавляет phrase или regex rule в in-memory registry.")
    @ApiResponse(
        responseCode = "200",
        description = "Custom rule создано",
        content = [Content(schema = Schema(implementation = CustomRuleResponse::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Ошибка validation custom rule",
        content = [Content(schema = Schema(implementation = JsonError::class))]
    )
    open fun create(@Body @Valid request: CustomRuleCreateRequest): Mono<CustomRuleResponse> =
        Mono.fromSupplier {
            customRuleRegistry
                .create(request)
                .toResponse()
        }

    /**
     * Возвращает rules в стабильном порядке их создания.
     */
    @Get
    @Operation(summary = "Получить custom rules", description = "Возвращает все custom rules в порядке создания.")
    @ApiResponse(
        responseCode = "200",
        description = "Custom rules получены",
        content = [Content(schema = Schema(implementation = Array<CustomRuleResponse>::class))]
    )
    open fun list(): Mono<List<CustomRuleResponse>> =
        Mono.fromSupplier {
            customRuleRegistry
                .list()
                .map { rule -> rule.toResponse() }
        }

    /**
     * Удаляет rule по identifier. Повторное удаление неизвестного id также успешно.
     */
    @Delete("/{id}")
    @Operation(summary = "Удалить custom rule", description = "Удаляет custom rule по identifier idempotently.")
    @ApiResponse(responseCode = "204", description = "Custom rule удалено или уже отсутствует")
    open fun delete(@PathVariable id: String): Mono<HttpResponse<Any>> =
        Mono.fromSupplier {
        customRuleRegistry.delete(id)

            HttpResponse.noContent()
        }
}
