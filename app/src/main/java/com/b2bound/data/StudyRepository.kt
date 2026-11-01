package com.b2bound.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StudyRepository @Inject constructor(private val dao: StudyDao) {
    fun savedVideos(): Flow<List<StudyVideoEntity>> = dao.observeVideos()
    fun notes(videoId: String): Flow<List<VideoNoteEntity>> = dao.observeNotes(videoId)
    suspend fun saveVideo(video: StudyVideoEntity) = dao.saveVideo(video)
    suspend fun addNote(videoId: String, text: String, seconds: Int) = dao.addNote(
        VideoNoteEntity(videoId = videoId, text = text.trim(), timestampSeconds = seconds.coerceAtLeast(0))
    )
}
