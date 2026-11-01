package com.b2bound.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(primary = Color(0xFF3659D9), secondary = Color(0xFF00A989), background = Color(0xFFF8F9FC), surface = Color.White)
private val Dark = darkColorScheme(primary = Color(0xFFADC0FF), secondary = Color(0xFF63D7B9))

@Composable fun B2BoundTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Dark else Light, content = content)
}
