package com.ryabov.promptfirewall.configuration

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("firewall")
class FirewallProperties {
    var analyzerTimeoutMs: Long = 500
    var maxPromptLength: Int = 12000
    var reviewThreshold: Int = 30
    var blockThreshold: Int = 60
}
