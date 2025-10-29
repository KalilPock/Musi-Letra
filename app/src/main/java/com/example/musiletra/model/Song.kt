
package com.example.musiletra.model

import java.util.UUID

data class Song(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val artist: String = "",
    val lyrics: String
)

