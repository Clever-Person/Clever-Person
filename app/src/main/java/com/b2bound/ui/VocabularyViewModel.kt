package com.b2bound.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b2bound.data.WordEntity
import com.b2bound.data.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class VocabularyViewModel @Inject constructor(private val repository: WordRepository) : ViewModel() {
    val words = repository.words().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val dueWords = repository.dueWords().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun add(term: String, definition: String, example: String) = viewModelScope.launch { repository.add(term, definition, example) }
    fun review(word: WordEntity, quality: Int) = viewModelScope.launch { repository.review(word, quality) }
}
