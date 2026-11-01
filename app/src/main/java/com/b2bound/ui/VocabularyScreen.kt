package com.b2bound.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.b2bound.data.WordEntity
import java.util.Locale

@Composable
fun VocabularyScreen(modifier: Modifier = Modifier, viewModel: VocabularyViewModel) {
    val savedWords by viewModel.words.collectAsState()
    val words by viewModel.dueWords.collectAsState()
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var showAdd by remember { mutableStateOf(false) }
    var active by remember(words) { mutableIntStateOf(0) }
    var revealed by remember(active) { mutableStateOf(false) }
    DisposableEffect(Unit) {
        val engine = TextToSpeech(context) { status -> if (status == TextToSpeech.SUCCESS) tts?.language = Locale.US }
        tts = engine
        onDispose { engine.stop(); engine.shutdown(); tts = null }
    }
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("Word trainer", style = MaterialTheme.typography.headlineMedium); Text("Review a little every day.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            FilledTonalIconButton(onClick = { showAdd = true }) { Icon(Icons.Default.Add, "Add a word") }
        }
            Text("${savedWords.size} saved · ${words.size} due today", style = MaterialTheme.typography.labelLarge)
        if (savedWords.isEmpty()) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) { Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Start your word list", style = MaterialTheme.typography.titleLarge)
                Text("Add a word you want to remember. Your list is saved on this device.")
                Button(onClick = { showAdd = true }) { Text("Add your first word") }
            } }
        } else if (words.isEmpty()) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) { Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("You’re all caught up!", style = MaterialTheme.typography.titleLarge)
                Text("Your next review is scheduled for another day. Add a word if you want to keep learning.")
                OutlinedButton(onClick = { showAdd = true }) { Text("Add a word") }
            } }
        } else {
            val word = words[active.coerceIn(words.indices)]
            Card(Modifier.fillMaxWidth().weight(1f), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("CARD ${active + 1} OF ${words.size}", style = MaterialTheme.typography.labelLarge); Text(word.source, style = MaterialTheme.typography.labelMedium) }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(word.term, style = MaterialTheme.typography.headlineLarge)
                        if (revealed) {
                            Text(word.definition, style = MaterialTheme.typography.titleMedium)
                            Text("“${word.example}”", style = MaterialTheme.typography.bodyLarge)
                        } else Text("Think of the meaning, then tap reveal.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { tts?.language = Locale.US; tts?.speak(word.term, TextToSpeech.QUEUE_FLUSH, null, "word-${word.id}") }) { Icon(Icons.Default.VolumeUp, null); Spacer(Modifier.width(6.dp)); Text("Listen · US") }
                        Button(onClick = { if (revealed) { active = (active + 1) % words.size; revealed = false } else revealed = true }) { Text(if (revealed) "Next word" else "Reveal") }
                    }
                }
            }
            if (revealed) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReviewButton("Again", { viewModel.review(word, 1); active = (active + 1) % words.size; revealed = false }, Modifier.weight(1f))
                ReviewButton("Hard", { viewModel.review(word, 3); active = (active + 1) % words.size; revealed = false }, Modifier.weight(1f))
                ReviewButton("Easy", { viewModel.review(word, 5); active = (active + 1) % words.size; revealed = false }, Modifier.weight(1f))
            }
        }
        if (savedWords.isNotEmpty()) Text("Your cards stay available offline", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    if (showAdd) AddWordDialog(onDismiss = { showAdd = false }, onSave = { t, d, e -> viewModel.add(t, d, e); showAdd = false })
}

@Composable private fun ReviewButton(label: String, onClick: () -> Unit, modifier: Modifier) { OutlinedButton(onClick = onClick, modifier = modifier) { Text(label) } }

@Composable private fun AddWordDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var term by remember { mutableStateOf("") }; var definition by remember { mutableStateOf("") }; var example by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Add a word") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(term, { term = it }, label = { Text("Word") }, singleLine = true)
            OutlinedTextField(definition, { definition = it }, label = { Text("Short meaning") })
            OutlinedTextField(example, { example = it }, label = { Text("Example sentence") })
        }
    }, confirmButton = { Button(onClick = { onSave(term, definition, example) }, enabled = term.isNotBlank() && definition.isNotBlank()) { Text("Save") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
