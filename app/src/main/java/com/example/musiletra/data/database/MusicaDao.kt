package com.example.musiletra.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicaDao {
    @Insert
    suspend fun salvarMusica(musica: MusicaSalva)

    @Delete
    suspend fun removerMusica(musica: MusicaSalva)
    
    @Update
    suspend fun atualizarMusica(musica: MusicaSalva)

    // Consulta mágica que busca músicas para um usuário específico
    // OU busca músicas do convidado (onde usuarioId é NULL)
    @Query("SELECT * FROM musicas_salvas WHERE usuarioId = :idDoUsuarioLogado OR (:idDoUsuarioLogado IS NULL AND usuarioId IS NULL)")
    fun getPlaylistDoUsuario(idDoUsuarioLogado: Int?): Flow<List<MusicaSalva>>
}