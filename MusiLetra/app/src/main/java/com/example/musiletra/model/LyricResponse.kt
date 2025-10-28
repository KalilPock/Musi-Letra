package com.example.musiletra.model

// Classe para representar a resposta JSON da API Lyrics.ovh
data class LyricResponse(
    val lyrics: String? = null, // Campo para a letra da música
    val error: String? = null    // Campo para mensagens de erro da API
)