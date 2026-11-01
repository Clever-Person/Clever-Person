package com.b2bound.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_videos")
data class StudyVideoEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val topic: String = "Conversation",
    val studyBand: String = "B1–B2",
    val addedAtEpochMillis: Long = System.currentTimeMillis()
)
