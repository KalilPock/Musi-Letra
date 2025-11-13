package com.example.musiletra.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val email: String,
    val senhaHash: String // (Lembre-se de usar um hash, não senha pura)
)