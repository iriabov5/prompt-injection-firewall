package com.ryabov.promptfirewall.configuration

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.service.PromptFirewallService
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
class MicronautDependencyInjectionIntegrationTest {

    @Inject
    lateinit var analyzers: List<PromptRiskAnalyzer>

    @Inject
    lateinit var promptFirewallService: PromptFirewallService

    @Test
    fun `injects analyzer beans into application context`() {
        assertEquals(5, analyzers.size)
    }

    @Test
    fun `creates prompt firewall service through factory`() {
        assertNotNull(promptFirewallService)
    }
}
