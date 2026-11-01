package com.b2bound.data

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepository @Inject constructor(private val dao: WordDao) {
    fun words(): Flow<List<WordEntity>> = dao.observeAll()
    fun dueWords(): Flow<List<WordEntity>> = dao.observeDue(LocalDate.now().toEpochDay())
    suspend fun add(term: String, definition: String, example: String) {
        dao.insert(WordEntity(term = term.trim(), definition = definition.trim(), example = example.trim(), dueAtEpochDay = LocalDate.now().toEpochDay()))
    }
    suspend fun review(word: WordEntity, quality: Int) {
        val plan = SpacedRepetition.review(word.intervalDays, word.repetitions, word.easeFactor, quality)
        dao.updateReview(word.id, plan.intervalDays, plan.repetitions, plan.easeFactor, LocalDate.now().plusDays(plan.intervalDays.toLong()).toEpochDay())
    }
    suspend fun count(): Int = dao.count()
}
