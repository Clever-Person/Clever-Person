package com.b2bound.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

data class StudyTarget(
    val title: String,
    val videoId: String? = null,
    val playlistId: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun B2BoundRoot() {
    var target by remember { mutableStateOf<StudyTarget?>(null) }
    val viewModel: StudyViewModel = hiltViewModel()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(target?.title ?: "B2 Bound") },
            navigationIcon = {
                if (target != null) IconButton(onClick = { target = null }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to playlists")
                } else Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.padding(start = 16.dp))
            }
        )
    }) { padding ->
        val current = target
        if (current == null) ChannelHubScreen(Modifier.padding(padding), viewModel) { target = it }
        else StudyPlayerScreen(Modifier.padding(padding), current, viewModel)
    }
}
