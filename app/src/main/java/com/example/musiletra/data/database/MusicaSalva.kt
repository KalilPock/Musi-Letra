package com.example.musiletra.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "musicas_salvas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Se apagar o usuário, apaga as músicas
        )
    ]
)
data class MusicaSalva(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Chave estrangeira: null se for "convidado" (usuário pulou o login)
    val usuarioId: Int?,

    // Dados da música
    val titulo: String,
    val artista: String,
    val letra: String,
    val apiSongId: String? // ID da música na API AUDD, se houver
)