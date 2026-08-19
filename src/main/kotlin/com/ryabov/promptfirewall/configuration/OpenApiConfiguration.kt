package com.ryabov.promptfirewall.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType

/**
 * Metadata OpenAPI specification, которую Micronaut OpenAPI использует при генерации контракта.
 */
@OpenAPIDefinition(
    info = Info(
        title = "Prompt Injection Firewall API",
        version = "0.1.0",
        description = "HTTP API для анализа prompts на признаки prompt injection и jailbreak-атак.",
        contact = Contact(name = "Ryabov Ivan", email = "i.d.ryabov@gmail.com"),
        license = License(name = "MIT")
    )
)
@SecurityScheme(
    name = "ApiKeyAuth",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    paramName = "X-API-Key",
    description = "Machine-to-machine API key для защищенных endpoints анализа prompt."
)
object OpenApiConfiguration
