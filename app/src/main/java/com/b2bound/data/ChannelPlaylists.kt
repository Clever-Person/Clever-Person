package com.b2bound.data

data class ChannelPlaylist(
    val title: String,
    val topic: String,
    val playlistId: String,
    val courseLevel: String = "Channel course 1"
) {
    val url: String get() = "https://www.youtube.com/playlist?list=$playlistId"
}

/** Playlist links are from the verified channel's public study-plan descriptions. */
object ChannelPlaylists {
    const val CHANNEL_PLAYLISTS_URL = "https://www.youtube.com/@zAmericanEnglish/playlists"
    val all = listOf(
        ChannelPlaylist("Grammar course", "Grammar", "PLp22-4PivYmLebd3vD0_WfIteVUlSA44G"),
        ChannelPlaylist("Listening course", "Listening", "PLp22-4PivYmKGwdQda1LVJxE1p7BZYNgJ"),
        ChannelPlaylist("Conversation course", "Conversation", "PLp22-4PivYmKp2aC7PI7K0POsf812xLBN"),
        ChannelPlaylist("Reading and vocabulary", "Vocabulary", "PLp22-4PivYmI4PahZ0eyFjcihT7_VAs79"),
        ChannelPlaylist("American pronunciation", "Listening", "PLp22-4PivYmIiXGqtQMrfE_tzmqE6r7Tc")
    )
}
