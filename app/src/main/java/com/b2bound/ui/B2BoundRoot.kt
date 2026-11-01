package com.b2bound.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

private enum class Tab(val label: String) { Home("Home"), Vocabulary("Words"), Practice("Practice"), Progress("Progress") }

@Composable
fun B2BoundRoot() {
    var selected by remember { mutableStateOf(Tab.Home) }
    Scaffold(bottomBar = {
        NavigationBar {
            Tab.entries.forEach { tab ->
                NavigationBarItem(selected = selected == tab, onClick = { selected = tab },
                    icon = { Icon(if (tab == Tab.Home) Icons.Default.Home else Icons.Default.AutoStories, contentDescription = null) },
                    label = { Text(tab.label) })
            }
        }
    }) { padding ->
        when (selected) {
            Tab.Home -> HomeScreen(Modifier.padding(padding), onStartWords = { selected = Tab.Vocabulary })
            Tab.Vocabulary -> VocabularyScreen(Modifier.padding(padding), hiltViewModel())
            Tab.Practice -> PlaceholderScreen("Practice", "Shadowing and roleplay are next in the build plan.", Modifier.padding(padding))
            Tab.Progress -> PlaceholderScreen("Your progress", "Your streak, study time, and weekly report will appear here.", Modifier.padding(padding))
        }
    }
}
