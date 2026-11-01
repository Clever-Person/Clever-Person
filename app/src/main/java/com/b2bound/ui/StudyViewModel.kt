package com.b2bound.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b2bound.data.StudyRepository
import com.b2bound.data.StudyVideoEntity
import com.b2bound.data.VideoNoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class StudyViewModel @Inject constructor(private val repository: StudyRepository) : ViewModel() {
    val savedVideos: StateFlow<List<StudyVideoEntity>> = repository.savedVideos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun notes(videoId: String) = repository.notes(videoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<VideoNoteEntity>())

    fun saveVideo(video: StudyVideoEntity) = viewModelScope.launch { repository.saveVideo(video) }
    fun addNote(videoId: String, text: String, seconds: Int) = viewModelScope.launch {
        if (text.isNotBlank()) repository.addNote(videoId, text, seconds)
    }
}
