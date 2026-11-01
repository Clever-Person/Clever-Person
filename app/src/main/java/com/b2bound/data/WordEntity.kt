package com.b2bound.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val term: String,
    val definition: String,
    val example: String,
    val source: String = "Personal",
    val intervalDays: Int = 0,
    val repetitions: Int = 0,
    val easeFactor: Double = 2.5,
    val dueAtEpochDay: Long = 0
)
