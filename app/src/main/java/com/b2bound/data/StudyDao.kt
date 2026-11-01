package com.b2bound.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    @Query("SELECT * FROM study_videos ORDER BY addedAtEpochMillis DESC")
    fun observeVideos(): Flow<List<StudyVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVideo(video: StudyVideoEntity)

    @Query("SELECT * FROM video_notes WHERE videoId = :videoId ORDER BY createdAtEpochMillis DESC")
    fun observeNotes(videoId: String): Flow<List<VideoNoteEntity>>

    @Insert
    suspend fun addNote(note: VideoNoteEntity)
}
