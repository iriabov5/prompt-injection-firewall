package com.ryabov.promptfirewall.security

import io.micronaut.context.annotation.ConfigurationProperties

/**
 * Настройки API key authentication для machine-to-machine вызовов firewall.
 */
@ConfigurationProperties("security.api-key")
class ApiKeySecurityProperties {
    /** Включает проверку API key для protected routes. */
    var enabled: Boolean = true

    /** Имя HTTP header, из которого читается API key. */
    var headerName: String = "X-API-Key"

    /** Список ключей из external configuration; пустые значения не считаются валидными. */
    var keys: List<String> = emptyList()

    /**
     * Возвращает только непустые ключи, чтобы пустой env placeholder не открывал API.
     */
    fun validKeys(): Set<String> = keys
        .map(String::trim)
        .filter(String::isNotEmpty)
        .toSet()
}
