package com.ryabov.promptfirewall.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScoreNormalizerTest {

    private val normalizer = ScoreNormalizer()

    @Test
    fun `returns zero when score is below minimum`() {
        assertEquals(0, normalizer.normalize(-10))
    }

    @Test
    fun `returns one hundred when score is above maximum`() {
        assertEquals(100, normalizer.normalize(125))
    }

    @Test
    fun `returns original score when score is inside range`() {
        assertEquals(42, normalizer.normalize(42))
    }
}
