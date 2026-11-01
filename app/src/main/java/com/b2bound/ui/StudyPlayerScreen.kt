package com.b2bound.ui

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.b2bound.data.VideoNoteEntity
import java.util.Locale
import kotlinx.coroutines.flow.flowOf

@Composable
fun StudyPlayerScreen(modifier: Modifier = Modifier, target: StudyTarget, viewModel: StudyViewModel) {
    val context = LocalContext.current
    var webView by remember(target) { mutableStateOf<WebView?>(null) }
    var currentVideoId by remember(target) { mutableStateOf(target.videoId.orEmpty()) }
    var noteText by remember { mutableStateOf("") }
    var speedIndex by remember { mutableIntStateOf(2) }
    var loopStart by remember(target) { mutableStateOf<Double?>(null) }
    var loopActive by remember(target) { mutableStateOf(false) }
    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
    val noteFlow = remember(currentVideoId) { if (currentVideoId.isBlank()) flowOf(emptyList()) else viewModel.notes(currentVideoId) }
    val notes by noteFlow.collectAsState(initial = emptyList())

    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Watch and learn", style = MaterialTheme.typography.headlineSmall)
        Text("Official YouTube player · video and notes stay linked", color = MaterialTheme.colorScheme.onSurfaceVariant)
        YouTubeIFrame(target = target, onVideoChanged = { currentVideoId = it }, onCreated = { webView = it })

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(onClick = {
                val next = (speedIndex + 1) % speeds.size
                speedIndex = next
                webView?.evaluateJavascript("player.setPlaybackRate(${speeds[next]})", null)
            }) { Text("Speed · ${speeds[speedIndex]}×") }
            FilledTonalButton(onClick = { webView?.evaluateJavascript("replayTenSeconds()", null) }) {
                Icon(Icons.Default.Replay10, null); Spacer(Modifier.width(4.dp)); Text("Replay 10s")
            }
        }
        OutlinedButton(onClick = {
            val view = webView ?: return@OutlinedButton
            when {
                loopActive -> { view.evaluateJavascript("clearLoop()", null); loopStart = null; loopActive = false }
                loopStart == null -> view.evaluateJavascript("getCurrentTime()") { raw -> loopStart = raw.numberResult() }
                else -> view.evaluateJavascript("getCurrentTime()") { raw ->
                    val end = raw.numberResult()
                    val start = loopStart
                    if (end != null && start != null && end > start + 0.5) {
                        view.evaluateJavascript("setLoopRange($start,$end)", null)
                        loopActive = true
                    } else loopStart = null
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(when { loopActive -> "Stop loop"; loopStart != null -> "Tap to mark loop end"; else -> "Loop this part · tap start" })
        }
        Text("To loop a part, tap once at its start and again at its end.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        HorizontalDivider()
        Text("My notes", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("New word or sentence") },
            minLines = 2
        )
        Button(enabled = noteText.isNotBlank() && currentVideoId.isNotBlank(), onClick = {
            val view = webView ?: return@Button
            val id = currentVideoId
            val content = noteText.trim()
            view.evaluateJavascript("getCurrentTime()") { raw ->
                viewModel.addNote(id, content, raw.numberResult()?.toInt() ?: 0)
                noteText = ""
            }
        }) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(6.dp)); Text("Save note with time") }
        if (currentVideoId.isBlank()) Text("Start a video to save a note for it.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (notes.isEmpty()) Text("Your notes for this video will show here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        notes.forEach { note -> NoteCard(note) {
            webView?.evaluateJavascript("player.seekTo(${note.timestampSeconds}, true)", null)
        } }

        OutlinedButton(onClick = {
            val id = currentVideoId.ifBlank { target.videoId.orEmpty() }
            if (id.isNotBlank()) context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$id")))
            else if (target.playlistId != null) context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=${target.playlistId}")))
        }, modifier = Modifier.fillMaxWidth()) { Text("Open in YouTube") }
    }
}

@Composable
private fun YouTubeIFrame(target: StudyTarget, onVideoChanged: (String) -> Unit, onCreated: (WebView) -> Unit) {
    val context = LocalContext.current
    val main = remember { Handler(Looper.getMainLooper()) }
    val bridge = remember(onVideoChanged) {
        object {
            @JavascriptInterface fun onVideoChanged(videoId: String) {
                if (videoId.matches(Regex("[A-Za-z0-9_-]{11}"))) main.post { onVideoChanged(videoId) }
            }
        }
    }
    AndroidView(
        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                webViewClient = WebViewClient()
                addJavascriptInterface(bridge, "B2Bound")
                onCreated(this)
                loadDataWithBaseURL("https://appassets.androidplatform.net/", playerHtml(target), "text/html", "UTF-8", null)
            }
        },
        update = { onCreated(it) }
    )
}

private fun playerHtml(target: StudyTarget): String {
    val playerSetup = if (target.videoId != null) "videoId: '${target.videoId}'" else "playerVars: { listType: 'playlist', list: '${target.playlistId.orEmpty()}', playsinline: 1, origin: 'https://appassets.androidplatform.net' }"
    return """
        <!doctype html><html><head><meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
        <style>html,body,#player{margin:0;width:100%;height:100%;background:#000;overflow:hidden}</style></head>
        <body><div id="player"></div><script src="https://www.youtube.com/iframe_api"></script><script>
        var player, loopTimer=null;
        function onYouTubeIframeAPIReady(){ player=new YT.Player('player',{width:'100%',height:'100%',$playerSetup,
          events:{onReady:function(){notifyVideo()},onStateChange:function(){notifyVideo()}}}); }
        function notifyVideo(){try{var id=player.getVideoData().video_id;if(id&&window.B2Bound)B2Bound.onVideoChanged(id)}catch(e){}}
        function getCurrentTime(){return player?player.getCurrentTime():0}
        function replayTenSeconds(){if(player)player.seekTo(Math.max(0,player.getCurrentTime()-10),true)}
        function setLoopRange(start,end){clearLoop();loopTimer=setInterval(function(){if(player&&player.getCurrentTime()>=end)player.seekTo(start,true)},250)}
        function clearLoop(){if(loopTimer){clearInterval(loopTimer);loopTimer=null}}
        </script></body></html>
    """.trimIndent()
}

private fun String.numberResult(): Double? = trim().trim('"').toDoubleOrNull()

@Composable private fun NoteCard(note: VideoNoteEntity, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(note.timestampSeconds.toTimestamp(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
            Text(note.text, modifier = Modifier.weight(1f))
        }
    }
}

private fun Int.toTimestamp(): String = "%02d:%02d".format(this / 60, this % 60)
