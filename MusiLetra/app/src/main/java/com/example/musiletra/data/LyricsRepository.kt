package com.example.musiletra.data

import com.example.musiletra.model.AuddSongItem

class LyricsRepository {
    
    /**
     * Busca músicas por texto (artista, título ou trecho da letra)
     * @return Result com lista de músicas ou erro
     */
    suspend fun searchByText(query: String): Result<List<AuddSongItem>> {
        return try {
            if (query.isBlank()) {
                return Result.failure(Exception("Digite algo para buscar"))
            }
            
            val response = RetrofitClient.auddService.search(
                apiToken = RetrofitClient.AUDD_API_KEY,
                query = query.trim(),
                returnType = "lyrics"
            )
            
            when {
                response.status == "success" && !response.result.isNullOrEmpty() -> {
                    // Filtra resultados que têm letra
                    val songsWithLyrics = response.result.filter { 
                        it.lyrics?.lyrics?.isNotBlank() == true ||
                        it.lyrics?.fullLyrics?.isNotBlank() == true
                    }
                    
                    if (songsWithLyrics.isNotEmpty()) {
                        Result.success(songsWithLyrics)
                    } else {
                        Result.failure(Exception("Nenhuma música com letra encontrada"))
                    }
                }
                response.status == "error" -> {
                    Result.failure(Exception("Erro na API do AudD"))
                }
                else -> {
                    Result.failure(Exception("Nenhuma música encontrada"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro na busca: ${e.message}"))
        }
    }
    
    /**
     * Busca letra específica por artista e título
     * @return Result com a letra ou erro
     */
    suspend fun findLyrics(artist: String, title: String): Result<Pair<String, String>> {
        return try {
            if (artist.isBlank() || title.isBlank()) {
                return Result.failure(Exception("Artista e título são obrigatórios"))
            }
            
            val response = RetrofitClient.auddService.findLyrics(
                apiToken = RetrofitClient.AUDD_API_KEY,
                query = "${artist.trim()} ${title.trim()}"
            )
            
            when {
                response.status == "success" && response.result != null -> {
                    val lyrics = response.result.lyrics?.fullLyrics 
                        ?: response.result.lyrics?.lyrics
                    
                    if (lyrics.isNullOrBlank()) {
                        Result.failure(Exception("Letra não disponível"))
                    } else {
                        // Retorna (título_real, letra)
                        val realTitle = response.result.title ?: title
                        Result.success(Pair(realTitle, lyrics))
                    }
                }
                response.status == "error" -> {
                    Result.failure(Exception("Erro na API do AudD"))
                }
                else -> {
                    Result.failure(Exception("Música não encontrada"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao buscar letra: ${e.message}"))
        }
    }
}