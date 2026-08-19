package com.ryabov.promptfirewall.security

import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.validator.TokenValidator
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

/**
 * Валидирует API key из configuration без логирования и хранения значения в пользовательском ответе.
 */
@Singleton
class ApiKeyTokenValidator(
    private val apiKeySecurityProperties: ApiKeySecurityProperties
) : TokenValidator<HttpRequestPlaceholder> {

    override fun validateToken(token: String, request: HttpRequestPlaceholder?): Publisher<Authentication> {
        val validKeys = apiKeySecurityProperties.validKeys()

        return if (apiKeySecurityProperties.enabled && token in validKeys && request.containsApiKeyHeader(token)) {
            Mono.just(Authentication.build(API_KEY_PRINCIPAL))
        } else {
            Mono.empty()
        }
    }

    private fun HttpRequestPlaceholder?.containsApiKeyHeader(token: String): Boolean =
        this
            ?.headers
            ?.get(apiKeySecurityProperties.headerName)
            ?.trim() == token

    private companion object {
        const val API_KEY_PRINCIPAL = "api-key-client"
    }
}

typealias HttpRequestPlaceholder = io.micronaut.http.HttpRequest<*>
