package com.example.musiletra.data

import com.example.musiletra.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data classes to model the JSON response from AudD API
data class AudDResponse(
    val status: String,
    val result: List<AudDSong>?
)

data class AudDSong(
    val title: String,
    val artist: String,
    val lyrics: String
)

// Retrofit service interface
interface AudDApiService {
    @GET("/")
    suspend fun findByLyrics(
        @Query("q") query: String,
        @Query("method") method: String = "findLyrics",
        @Query("api_token") apiToken: String = BuildConfig.AUDD_API_KEY
    ): AudDResponse
}

// Retrofit client singleton
object RetrofitClient {
    private const val BASE_URL = "https://api.audd.io/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: AudDApiService by lazy {
        retrofit.create(AudDApiService::class.java)
    }
}
