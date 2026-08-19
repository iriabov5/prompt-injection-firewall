package com.ryabov.promptfirewall.configuration

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.service.PromptFirewallService
import com.ryabov.promptfirewall.service.RiskAggregator
import com.ryabov.promptfirewall.service.ScoreNormalizer
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.time.Duration

@Factory
class PromptFirewallFactory {

    @Singleton
    fun scoreNormalizer(): ScoreNormalizer = ScoreNormalizer()

    @Singleton
    fun riskAggregator(
        scoreNormalizer: ScoreNormalizer,
        firewallProperties: FirewallProperties
    ): RiskAggregator = RiskAggregator(
        scoreNormalizer = scoreNormalizer,
        reviewThreshold = firewallProperties.reviewThreshold,
        blockThreshold = firewallProperties.blockThreshold
    )

    @Singleton
    fun promptFirewallService(
        analyzers: List<PromptRiskAnalyzer>,
        riskAggregator: RiskAggregator,
        firewallProperties: FirewallProperties
    ): PromptFirewallService = PromptFirewallService(
        analyzers = analyzers,
        riskAggregator = riskAggregator,
        analyzerTimeout = Duration.ofMillis(firewallProperties.analyzerTimeoutMs)
    )
}
