package com.b2bound.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpacedRepetitionTest {
    @Test fun successfulFirstReviewSchedulesOneDay() {
        val plan = SpacedRepetition.review(0, 0, 2.5, 4)
        assertEquals(1, plan.intervalDays)
        assertEquals(1, plan.repetitions)
    }

    @Test fun secondSuccessfulReviewSchedulesSixDays() {
        val plan = SpacedRepetition.review(1, 1, 2.5, 4)
        assertEquals(6, plan.intervalDays)
        assertEquals(2, plan.repetitions)
    }

    @Test fun failedRecallRestartsRepetitionCountAndKeepsEaseValid() {
        val plan = SpacedRepetition.review(12, 4, 1.3, 1)
        assertEquals(1, plan.intervalDays)
        assertEquals(0, plan.repetitions)
        assertTrue(plan.easeFactor >= 1.3)
    }

    @Test(expected = IllegalArgumentException::class) fun rejectsInvalidQuality() {
        SpacedRepetition.review(1, 0, 2.5, 7)
    }
}
