package com.example.musiletra.model

import com.google.gson.annotations.SerializedName

// Resposta da busca geral
data class AuddSearchResponse(
    val status: String,
    val result: List<AuddSongItem>?
)

data class AuddSongItem(
    val artist: String?,
    val title: String?,
    val album: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val label: String?,
    @SerializedName("song_link")
    val songLink: String?,
    val lyrics: AuddLyricsData?
)

data class AuddLyricsData(
    val lyrics: String?,
    @SerializedName("full_lyrics")
    val fullLyrics: String?
)

// Resposta para findLyrics (busca específica)
data class AuddFindLyricsResponse(
    val status: String,
    val result: AuddFindResult?
)

data class AuddFindResult(
    val artist: String?,
    val title: String?,
    val album: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val lyrics: AuddLyricsDetail?
)

data class AuddLyricsDetail(
    val lyrics: String?,
    @SerializedName("full_lyrics")
    val fullLyrics: String?
)