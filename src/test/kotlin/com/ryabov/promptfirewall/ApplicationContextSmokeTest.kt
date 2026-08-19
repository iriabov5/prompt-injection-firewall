package com.ryabov.promptfirewall

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class ApplicationContextSmokeTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun `application context starts`() {
        assertTrue(application.isRunning)
    }
}
