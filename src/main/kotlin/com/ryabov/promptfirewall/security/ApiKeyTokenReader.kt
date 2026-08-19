package com.ryabov.promptfirewall.security

import io.micronaut.core.order.Ordered
import io.micronaut.http.HttpRequest
import io.micronaut.security.token.reader.TokenReader
import jakarta.inject.Singleton
import java.util.Optional

/**
 * Читает API key из настраиваемого HTTP header и передает его в Micronaut Security token pipeline.
 */
@Singleton
class ApiKeyTokenReader(
    private val apiKeySecurityProperties: ApiKeySecurityProperties
) : TokenReader<HttpRequest<*>> {

    override fun findToken(request: HttpRequest<*>): Optional<String> =
        Optional.ofNullable(request.headers.get(apiKeySecurityProperties.headerName))
            .map(String::trim)
            .filter(String::isNotEmpty)

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}
