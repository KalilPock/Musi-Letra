package com.example.musiletra.data

import com.example.musiletra.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit service interface
interface AudDApiService {
    @GET("/")
    suspend fun findByLyrics(
        @Query("q") query: String,
        @Query("method") method: String = "findLyrics",
        @Query("api_token") apiToken: String = BuildConfig.AUDD_API_KEY
    ): AudDResponse
}
