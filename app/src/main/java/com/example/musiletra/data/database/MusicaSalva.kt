package com.example.musiletra.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musicas_salvas")
data class MusicaSalva(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val artista: String,
    val letra: String
)
