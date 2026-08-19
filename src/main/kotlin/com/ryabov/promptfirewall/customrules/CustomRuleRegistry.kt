package com.ryabov.promptfirewall.customrules

import com.ryabov.promptfirewall.configuration.CustomRulesProperties
import com.ryabov.promptfirewall.model.CustomRuleCreateRequest
import com.ryabov.promptfirewall.model.CustomRuleType
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import java.util.LinkedHashMap
import java.util.UUID

/**
 * In-memory registry пользовательских правил. Все операции синхронизированы,
 * чтобы список правил оставался консистентным и сохранял порядок создания.
 */
@Singleton
class CustomRuleRegistry(
    private val properties: CustomRulesProperties
) {
    private val rules = LinkedHashMap<String, CustomRule>()

    @Synchronized
    fun create(request: CustomRuleCreateRequest): CustomRule {
        if (rules.size >= properties.maxRules) {
            throw badRequest("Custom rules limit is reached")
        }

        validate(request)

        val id = UUID.randomUUID().toString()
        val rule = customRule {
            this.id = id
            from(request)
        }

        rules[id] = rule

        return rule
    }

    @Synchronized
    fun list(): List<CustomRule> =
        rules.values.toList()

    @Synchronized
    fun delete(id: String) {
        rules.remove(id)
    }

    private fun validate(request: CustomRuleCreateRequest) {
        requireWithinLimit(request.code, properties.maxCodeLength, "code")
        requireWithinLimit(request.description, properties.maxDescriptionLength, "description")

        when (request.type) {
            CustomRuleType.PHRASE -> validatePhraseRule(request)
            CustomRuleType.REGEX -> validateRegexRule(request)
        }
    }

    private fun validatePhraseRule(request: CustomRuleCreateRequest) {
        val phrase = request.phrase?.trim()

        if (phrase.isNullOrEmpty()) {
            throw badRequest("Phrase rule requires non-empty phrase")
        }

        requireWithinLimit(phrase, properties.maxPatternLength, "phrase")
    }

    private fun validateRegexRule(request: CustomRuleCreateRequest) {
        val pattern = request.pattern?.trim()

        if (pattern.isNullOrEmpty()) {
            throw badRequest("Regex rule requires non-empty pattern")
        }

        requireWithinLimit(pattern, properties.maxPatternLength, "pattern")

        try {
            Regex(pattern)
        } catch (exception: IllegalArgumentException) {
            throw badRequest("Regex pattern is invalid")
        }
    }

    private fun requireWithinLimit(value: String, limit: Int, field: String) {
        if (value.trim().length > limit) {
            throw badRequest("$field exceeds configured length limit")
        }
    }

    private fun badRequest(message: String): HttpStatusException =
        HttpStatusException(HttpStatus.BAD_REQUEST, message)
}
