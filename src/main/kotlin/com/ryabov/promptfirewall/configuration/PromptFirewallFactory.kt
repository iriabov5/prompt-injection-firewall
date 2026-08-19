package com.ryabov.promptfirewall.configuration

import com.ryabov.promptfirewall.analyzer.PromptRiskAnalyzer
import com.ryabov.promptfirewall.service.PromptFirewallService
import com.ryabov.promptfirewall.service.RiskAggregator
import com.ryabov.promptfirewall.service.ScoreNormalizer
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.time.Duration

/**
 * Micronaut factory, которая собирает core-компоненты firewall через DI и
 * передает runtime-настройки в scoring и analyzer orchestration.
 */
@Factory
class PromptFirewallFactory {

    /**
     * Создает нормализатор score как отдельный bean для явной композиции scoring layer.
     */
    @Singleton
    fun scoreNormalizer(): ScoreNormalizer = ScoreNormalizer()

    /**
     * Создает aggregator с порогами из конфигурации приложения.
     */
    @Singleton
    fun riskAggregator(
        scoreNormalizer: ScoreNormalizer,
        firewallProperties: FirewallProperties
    ): RiskAggregator = RiskAggregator(
        scoreNormalizer = scoreNormalizer,
        reviewThreshold = firewallProperties.reviewThreshold,
        blockThreshold = firewallProperties.blockThreshold
    )

    /**
     * Создает сервис анализа с набором analyzer beans, найденных Micronaut context.
     */
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
