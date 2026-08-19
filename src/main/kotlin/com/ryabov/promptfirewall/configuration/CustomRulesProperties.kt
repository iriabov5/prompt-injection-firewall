package com.ryabov.promptfirewall.configuration

import io.micronaut.context.annotation.ConfigurationProperties

/**
 * Лимиты in-memory custom rules, которые защищают runtime от слишком большого
 * количества правил и чрезмерно длинных пользовательских pattern values.
 */
@ConfigurationProperties("firewall.custom-rules")
class CustomRulesProperties {
    var maxRules: Int = 100
    var maxCodeLength: Int = 64
    var maxPatternLength: Int = 512
    var maxDescriptionLength: Int = 256
}
