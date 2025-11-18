package com.example.musiletra.data

import com.example.musiletra.model.AudDApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
