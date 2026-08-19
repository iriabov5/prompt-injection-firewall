package com.ryabov.promptfirewall.service

/**
 * Ограничивает числовой риск диапазоном, который понимают API и scoring rules.
 */
class ScoreNormalizer {

    /**
     * Возвращает score в диапазоне от [MIN_SCORE] до [MAX_SCORE].
     */
    fun normalize(score: Int): Int = score.coerceIn(MIN_SCORE, MAX_SCORE)

    companion object {
        const val MIN_SCORE = 0
        const val MAX_SCORE = 100
    }
}
