package com.ryabov.promptfirewall.security

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

/**
 * Добавляет baseline security headers ко всем HTTP responses приложения.
 */
@Filter("/**")
class SecurityHeadersFilter : HttpServerFilter {

    override fun doFilter(
        request: HttpRequest<*>,
        chain: ServerFilterChain
    ): Publisher<MutableHttpResponse<*>> =
        Flux.from(chain.proceed(request))
            .map { response: MutableHttpResponse<*> ->
                response.header("X-Content-Type-Options", "nosniff")
                response.header("X-Frame-Options", "DENY")
                response.header("Referrer-Policy", "no-referrer")
                response
            }
}
