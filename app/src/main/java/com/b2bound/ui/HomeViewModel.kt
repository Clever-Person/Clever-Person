package com.b2bound.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b2bound.data.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WordRepository) : ViewModel() {
    private val _wordCount = MutableStateFlow(0)
    val wordCount: StateFlow<Int> = _wordCount
    init { viewModelScope.launch { _wordCount.value = repository.count() } }
}
