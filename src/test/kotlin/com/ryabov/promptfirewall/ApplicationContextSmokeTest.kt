package com.ryabov.promptfirewall

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MicronautTest
@DisplayName("Smoke-тест запуска Micronaut context")
class ApplicationContextSmokeTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    @DisplayName("Приложение успешно поднимает Micronaut context")
    fun `application context starts`() {
        assertTrue(application.isRunning)
    }
}
