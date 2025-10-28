package com.example.musiletra.data

import com.example.musiletra.model.LyricResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
// Esta interface define os endpoints da API para o Retrofit
interface LyricApiService {

    // A anotação @GET define que esta função fará uma requisição HTTP GET.
    // O caminho "v1/{artist}/{title}" é a parte da URL que vem depois da URL base.
    // As anotações @Path substituem {artist} e {title} pelos valores passados para a função.
    @GET("v1/{artist}/{title}")
    suspend fun getLyrics(
        @Path("artist") artist: String,
        @Path("title") title: String
    ): LyricResponse // O Retrofit (com Gson) irá converter a resposta JSON neste objeto
}