package com.example.musiletra.data

import com.example.musiletra.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.audd.io/"
    
    // A chave vem do BuildConfig, gerado pelo Secrets Plugin
    // Nunca aparece diretamente no código!
    val AUDD_API_KEY: String = BuildConfig.AUDD_API_KEY

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Em produção, mude para BASIC ou NONE
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val auddService: AuddApiService = retrofit.create(AuddApiService::class.java)
}