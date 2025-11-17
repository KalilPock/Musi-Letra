package com.example.musiletra.data

import com.google.gson.annotations.SerializedName

// Data classes to model the JSON response from AudD API
data class AudDResponse(
    val status: String,
    val result: List<AudDSong>?,
    val error: AudDError? // Adicionado para capturar mensagens de erro
)

data class AudDSong(
    val title: String, val artist: String, val lyrics: String
)

// Classe para o objeto de erro da API
data class AudDError(
    @SerializedName("error_message") val message: String,
    @SerializedName("error_code") val code: Int
)
