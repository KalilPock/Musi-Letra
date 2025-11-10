package com.example.musiletra.model

data class Playlist(
    val id: Int,
    val name: String,
    var description: String? = null,
    var songs: List<String> // id das músicas
)
