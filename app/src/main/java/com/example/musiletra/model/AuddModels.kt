package com.example.musiletra.model

import com.example.musiletra.BuildConfig
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// Data classes to model the JSON response from AudD API
data class AudDResponse(
    val status: String,
    val result: List<AudDSong>?,
    val error: AudDError? // Adicionado para capturar mensagens de erro
)

data class AudDSong(
    val title: String,
    val artist: String,
    val lyrics: String
)

// Classe para o objeto de erro da API
data class AudDError(
    @SerializedName("error_message") val message: String,
    @SerializedName("error_code") val code: Int
)


// Retrofit service interface
public interface AudDApiService {
    @GET("/")
    suspend fun findByLyrics(
        @Query("q") query: String,
        @Query("method") method: String = "findLyrics",
        @Query("api_token") apiToken: String = BuildConfig.AUDD_API_KEY
    ): AudDResponse
}