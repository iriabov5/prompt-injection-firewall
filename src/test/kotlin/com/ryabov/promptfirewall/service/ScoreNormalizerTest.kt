package com.ryabov.promptfirewall.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Нормализация risk score")
class ScoreNormalizerTest {

    private val normalizer = ScoreNormalizer()

    @Test
    @DisplayName("Возвращает 0, если score ниже минимального значения")
    fun `returns zero when score is below minimum`() {
        assertEquals(0, normalizer.normalize(-10))
    }

    @Test
    @DisplayName("Возвращает 100, если score выше максимального значения")
    fun `returns one hundred when score is above maximum`() {
        assertEquals(100, normalizer.normalize(125))
    }

    @Test
    @DisplayName("Сохраняет score без изменений внутри допустимого диапазона")
    fun `returns original score when score is inside range`() {
        assertEquals(42, normalizer.normalize(42))
    }
}
