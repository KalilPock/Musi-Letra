package com.example.musiletra.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val descricao: String = "",
    val musicaIds: String = "" // IDs das músicas separados por vírgula (ex: "1,2,3")
)
