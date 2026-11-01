package com.b2bound.data

import android.net.Uri

object YouTubeLinkParser {
    private val idPattern = Regex("^[A-Za-z0-9_-]{11}$")

    fun videoId(input: String): String? {
        val value = input.trim()
        if (idPattern.matches(value)) return value
        val uri = runCatching { Uri.parse(value) }.getOrNull() ?: return null
        val host = uri.host?.lowercase().orEmpty()
        if (host !in setOf("youtube.com", "www.youtube.com", "m.youtube.com", "youtu.be", "www.youtu.be")) return null
        val id = when {
            host.endsWith("youtu.be") -> uri.lastPathSegment
            uri.path == "/watch" -> uri.getQueryParameter("v")
            uri.pathSegments.firstOrNull() in setOf("embed", "shorts", "live") -> uri.pathSegments.getOrNull(1)
            else -> null
        }
        return id?.takeIf(idPattern::matches)
    }
}
