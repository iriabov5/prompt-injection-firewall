package com.ryabov.promptfirewall.configuration

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.service.PromptFirewallService
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("Micronaut dependency injection для firewall runtime")
class MicronautDependencyInjectionIntegrationTest {

    @Inject
    lateinit var analyzers: List<PromptRiskAnalyzer>

    @Inject
    lateinit var promptFirewallService: PromptFirewallService

    @Test
    @DisplayName("Подключает все analyzer beans в application context")
    fun `injects analyzer beans into application context`() {
        assertEquals(5, analyzers.size)
    }

    @Test
    @DisplayName("Создает PromptFirewallService через Micronaut factory")
    fun `creates prompt firewall service through factory`() {
        assertNotNull(promptFirewallService)
    }
}
