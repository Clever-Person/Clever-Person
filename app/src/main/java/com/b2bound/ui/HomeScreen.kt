package com.b2bound.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Ink = Color(0xFF172B4D)
private val Blue = Color(0xFF3659D9)
private val PaleBlue = Color(0xFFEAF0FF)

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onStartWords: () -> Unit) {
    LazyColumn(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text("B2 Bound", style = MaterialTheme.typography.headlineMedium, color = Ink); Text("Your English, one day at a time", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Text("🇺🇸", style = MaterialTheme.typography.headlineLarge)
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = PaleBlue), shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Your B2 journey", style = MaterialTheme.typography.titleLarge, color = Ink); Text("B1  →  B2", color = Blue) }
                        Text("🔥 0 days", style = MaterialTheme.typography.titleMedium, color = Ink)
                    }
                    LinearProgressIndicator(progress = { 0.18f }, modifier = Modifier.fillMaxWidth().height(10.dp), color = Blue, trackColor = Color.White)
                    Text("18% complete · Keep your daily rhythm", style = MaterialTheme.typography.bodyMedium, color = Ink)
                }
            }
        }
        item { Text("Today · 20 minutes", style = MaterialTheme.typography.titleLarge, color = Ink) }
        item { PlanItem(Icons.Default.Style, "Vocabulary", "5 min · Learn 5 useful words", "Start", onStartWords) }
        item { PlanItem(Icons.Default.Headphones, "Listening", "5 min · Hear natural US English", "Open", {}) }
        item { PlanItem(Icons.Default.Mic, "Speaking", "5 min · Say it out loud", "Practice", {}) }
        item { PlanItem(Icons.Default.School, "Quick quiz", "5 min · Check what you know", "Begin", {}) }
        item {
            Card(shape = RoundedCornerShape(20.dp)) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Small steps add up", style = MaterialTheme.typography.titleMedium)
                Text("Finish today’s plan to build your streak. Your words and lessons work offline.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } }
        }
    }
}

@Composable private fun PlanItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, action: String, onClick: () -> Unit) {
    Card(shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(14.dp), color = PaleBlue) { Icon(icon, null, Modifier.padding(12.dp), tint = Blue) }
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) { Text(title, style = MaterialTheme.typography.titleMedium, color = Ink); Text(subtitle, style = MaterialTheme.typography.bodySmall) }
            TextButton(onClick = onClick) { Text(action) }
        }
    }
}

@Composable fun PlaceholderScreen(title: String, message: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(title, style = MaterialTheme.typography.headlineMedium); Spacer(Modifier.height(8.dp)); Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant) } }
}
