package com.example.musiletra.data

import com.example.musiletra.model.AuddSearchResponse
import com.example.musiletra.model.AuddFindLyricsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuddApiService {
    
    /**
     * Busca geral por texto (pode ser artista, título ou trecho da letra)
     * @param apiToken Token da API do AudD
     * @param query Texto de busca
     * @param returnType Tipo de retorno (lyrics, apple_music, spotify, etc)
     */
    @FormUrlEncoded
    @POST("/")
    suspend fun search(
        @Field("api_token") apiToken: String,
        @Field("q") query: String,
        @Field("return") returnType: String = "lyrics"
    ): AuddSearchResponse
    
    /**
     * Busca letra específica por artista e título
     * @param apiToken Token da API do AudD
     * @param query Query no formato "artista título"
     */
    @FormUrlEncoded
    @POST("/findLyrics/")
    suspend fun findLyrics(
        @Field("api_token") apiToken: String,
        @Field("q") query: String
    ): AuddFindLyricsResponse
}