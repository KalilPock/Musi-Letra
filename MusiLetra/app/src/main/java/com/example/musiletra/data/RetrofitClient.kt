package com.example.musiletra.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 'object' cria uma instância única (Singleton) para o cliente Retrofit
object RetrofitClient {

    // URL base da API Lyrics.ovh
    private const val BASE_URL = "https://api.lyrics.ovh/"

    // Cria um interceptor para log das requisições (útil para debug)
    // Isso mostrará detalhes da requisição e resposta no Logcat do Android Studio
    // ou no output do console se você rodar via terminal com log habilitado.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Define o nível de detalhe do log
    }

    // Cria o cliente OkHttp adicionando o interceptor de log
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Configura a instância principal do Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)                       // Define a URL base para todas as chamadas
        .client(okHttpClient)                    // Associa o cliente OkHttp (com logs)
        .addConverterFactory(GsonConverterFactory.create()) // Define o Gson como o conversor JSON
        .build()

    // Cria a implementação concreta da interface 'LyricApiService'
    // É esta instância que o seu Repository usará para fazer as chamadas à API
    val lyricApiService: LyricApiService = retrofit.create(LyricApiService::class.java)
}