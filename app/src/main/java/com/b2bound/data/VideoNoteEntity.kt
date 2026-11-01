package com.b2bound.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "video_notes", indices = [Index("videoId")])
data class VideoNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val videoId: String,
    val text: String,
    val timestampSeconds: Int,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)
