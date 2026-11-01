package com.b2bound.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.b2bound.data.ChannelPlaylists
import com.b2bound.data.ChannelPlaylist
import com.b2bound.data.StudyVideoEntity
import com.b2bound.data.YouTubeLinkParser

private val HubBlue = Color(0xFF3157D5)

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun ChannelHubScreen(modifier: Modifier = Modifier, viewModel: StudyViewModel, onOpen: (StudyTarget) -> Unit) {
    val context = LocalContext.current
    val saved by viewModel.savedVideos.collectAsState()
    var topic by remember { mutableStateOf("All topics") }
    var band by remember { mutableStateOf("All levels") }
    var addDialog by remember { mutableStateOf(false) }
    val topics = listOf("All topics", "Grammar", "Vocabulary", "Listening", "Conversation")
    val bands = listOf("All levels", "B1", "B2")
    val filteredPlaylists = ChannelPlaylists.all.filter { topic == "All topics" || it.topic == topic }
    val filteredVideos = saved.filter { (topic == "All topics" || it.topic == topic) && (band == "All levels" || it.studyBand == band || it.studyBand == "B1–B2") }

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF0FF)), shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Learn with ZAmerican English", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF172B4D))
                    Text("Choose a topic, watch in the official YouTube player, and save notes as you study.", color = Color(0xFF344563))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { addDialog = true }) { Icon(Icons.Default.AddLink, null); Spacer(Modifier.width(6.dp)); Text("Add a video") }
                        OutlinedButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ChannelPlaylists.CHANNEL_PLAYLISTS_URL))) }) { Text("All playlists") }
                    }
                }
            }
        }
        item {
            Text("Topic", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                topics.forEach { item -> FilterChip(selected = topic == item, onClick = { topic = item }, label = { Text(item) }) }
            }
        }
        item {
            Text("Study band for your videos", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                bands.forEach { item -> FilterChip(selected = band == item, onClick = { band = item }, label = { Text(item) }) }
            }
        }
        item { Text("Channel playlists", style = MaterialTheme.typography.titleLarge) }
        items(filteredPlaylists) { playlist -> PlaylistCard(playlist) { onOpen(StudyTarget(playlist.title, playlistId = playlist.playlistId)) } }
        item { Text("My videos · ${filteredVideos.size}", style = MaterialTheme.typography.titleLarge) }
        if (filteredVideos.isEmpty()) {
            item { Text("Add a YouTube video link to start your own B1–B2 study list.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else {
            items(filteredVideos, key = { it.videoId }) { video ->
                SavedVideoCard(video) { onOpen(StudyTarget(video.title, videoId = video.videoId)) }
            }
        }
        item { Text("Videos stay on YouTube. B2 Bound saves your links and study notes on this device.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }

    if (addDialog) AddVideoDialog(
        onDismiss = { addDialog = false },
        onSave = { video -> viewModel.saveVideo(video); addDialog = false; onOpen(StudyTarget(video.title, videoId = video.videoId)) }
    )
}

@Composable private fun PlaylistCard(playlist: ChannelPlaylist, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.PlayCircle, null, tint = HubBlue, modifier = Modifier.size(34.dp))
            Column(Modifier.weight(1f)) {
                Text(playlist.title, style = MaterialTheme.typography.titleMedium)
                Text("${playlist.topic} · ${playlist.courseLevel}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.OpenInNew, contentDescription = "Play playlist", tint = HubBlue)
        }
    }
}

@Composable private fun SavedVideoCard(video: StudyVideoEntity, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.PlayCircle, null, tint = HubBlue, modifier = Modifier.size(32.dp))
            Column(Modifier.weight(1f)) { Text(video.title, style = MaterialTheme.typography.titleMedium); Text("${video.topic} · ${video.studyBand}", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun AddVideoDialog(onDismiss: () -> Unit, onSave: (StudyVideoEntity) -> Unit) {
    var url by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("Conversation") }
    var band by remember { mutableStateOf("B1–B2") }
    val parsedId = YouTubeLinkParser.videoId(url)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a YouTube video") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(url, { url = it }, label = { Text("Video link") }, placeholder = { Text("youtube.com/watch?v=...") }, singleLine = true)
                OutlinedTextField(title, { title = it }, label = { Text("Title (optional)") }, singleLine = true)
                Text("Topic", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) { listOf("Grammar", "Vocabulary", "Listening", "Conversation").forEach { FilterChip(topic == it, { topic = it }, label = { Text(it) }) } }
                Text("Study band", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { listOf("B1", "B2", "B1–B2").forEach { FilterChip(band == it, { band = it }, label = { Text(it) }) } }
                if (url.isNotBlank() && parsedId == null) Text("Enter a valid YouTube video URL.", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            Button(enabled = parsedId != null, onClick = {
                val id = parsedId ?: return@Button
                val cleanTitle = title.trim().ifBlank { "Your video · ${id.take(5)}" }
                onSave(StudyVideoEntity(videoId = id, title = cleanTitle, topic = topic, studyBand = band))
            }) { Text("Save and watch") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
