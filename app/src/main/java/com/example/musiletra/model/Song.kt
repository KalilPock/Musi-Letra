
package com.example.musiletra.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val artist: String = "",
    val lyrics: String
)

