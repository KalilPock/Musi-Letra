package com.example.musiletra.model

data class Playlist(
    val id: Int,
    val name: String,
    var songs: List<Song>
)
