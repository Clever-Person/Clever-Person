package com.b2bound.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words ORDER BY dueAtEpochDay, term") fun observeAll(): Flow<List<WordEntity>>
    @Query("SELECT * FROM words WHERE dueAtEpochDay <= :today ORDER BY dueAtEpochDay, term") fun observeDue(today: Long): Flow<List<WordEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(word: WordEntity): Long
    @Query("UPDATE words SET intervalDays=:interval, repetitions=:repetitions, easeFactor=:ease, dueAtEpochDay=:due WHERE id=:id")
    suspend fun updateReview(id: Long, interval: Int, repetitions: Int, ease: Double, due: Long)
    @Query("SELECT COUNT(*) FROM words") suspend fun count(): Int
}
