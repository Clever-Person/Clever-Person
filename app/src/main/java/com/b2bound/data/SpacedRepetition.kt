package com.b2bound.data

data class ReviewPlan(val intervalDays: Int, val repetitions: Int, val easeFactor: Double)

/** SM-2 inspired review scheduler. Quality is 0..5; 3+ counts as a successful recall. */
object SpacedRepetition {
    fun review(intervalDays: Int, repetitions: Int, easeFactor: Double, quality: Int): ReviewPlan {
        require(quality in 0..5) { "Quality must be between 0 and 5" }
        val nextEase = (easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))).coerceAtLeast(1.3)
        if (quality < 3) return ReviewPlan(1, 0, nextEase)
        val nextRepetitions = repetitions + 1
        val nextInterval = when (nextRepetitions) {
            1 -> 1
            2 -> 6
            else -> (intervalDays * nextEase).toInt().coerceAtLeast(intervalDays + 1)
        }
        return ReviewPlan(nextInterval, nextRepetitions, nextEase)
    }
}
