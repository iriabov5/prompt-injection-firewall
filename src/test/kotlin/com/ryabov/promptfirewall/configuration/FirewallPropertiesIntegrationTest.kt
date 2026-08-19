package com.ryabov.promptfirewall.configuration

import com.ryabov.promptfirewall.model.RiskLevel
import com.ryabov.promptfirewall.service.RiskAggregator
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
@Property(name = "firewall.analyzer-timeout-ms", value = "250")
@Property(name = "firewall.review-threshold", value = "20")
@Property(name = "firewall.block-threshold", value = "50")
class FirewallPropertiesIntegrationTest {

    @Inject
    lateinit var firewallProperties: FirewallProperties

    @Inject
    lateinit var riskAggregator: RiskAggregator

    @Test
    fun `binds firewall properties from configuration`() {
        assertEquals(250, firewallProperties.analyzerTimeoutMs)
        assertEquals(12000, firewallProperties.maxPromptLength)
        assertEquals(20, firewallProperties.reviewThreshold)
        assertEquals(50, firewallProperties.blockThreshold)
    }

    @Test
    fun `configured thresholds are used by risk scoring components`() {
        assertEquals(RiskLevel.MEDIUM, riskAggregator.riskLevel(20))
        assertEquals(RiskLevel.HIGH, riskAggregator.riskLevel(50))
    }
}
