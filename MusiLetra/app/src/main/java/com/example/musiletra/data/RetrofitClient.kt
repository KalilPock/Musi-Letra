package com.example.musiletra.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// 'object' cria uma instância única (Singleton) para o cliente Retrofit
object RetrofitClient {

    // URL base da API Lyrics.ovh
    private const val BASE_URL = "https://api.lyrics.ovh/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    // Configura a instância principal do Retrofit usando o cliente padrão
    // (remove a dependência direta de okhttp3 e do interceptor de logging aqui)
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Define a URL base para todas as chamadas
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Define o Gson como o conversor JSON
            .build()
    }

    // Cria a implementação concreta da interface 'LyricApiService'
    // É esta instância que o seu Repository usará para fazer as chamadas à API
    val lyricApiService: LyricApiService by lazy {
        retrofit.create(LyricApiService::class.java)
    }
}