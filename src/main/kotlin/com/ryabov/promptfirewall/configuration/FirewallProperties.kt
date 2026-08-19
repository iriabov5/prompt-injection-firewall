package com.ryabov.promptfirewall.configuration

import io.micronaut.context.annotation.ConfigurationProperties

/**
 * Настройки эвристического firewall runtime, которые биндингуются из `firewall.*`
 * и управляют timeout анализаторов и порогами принятия решений.
 */
@ConfigurationProperties("firewall")
class FirewallProperties {
    var analyzerTimeoutMs: Long = 500
    var maxPromptLength: Int = 12000
    var reviewThreshold: Int = 30
    var blockThreshold: Int = 60
}
