package com.ryabov.promptfirewall.service

class ScoreNormalizer {

    fun normalize(score: Int): Int = score.coerceIn(MIN_SCORE, MAX_SCORE)

    companion object {
        const val MIN_SCORE = 0
        const val MAX_SCORE = 100
    }
}
