package com.example.musiletra.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicaDao {
    // CREATE
    @Insert
    suspend fun inserir(musica: MusicaSalva): Long

    // READ - todas as músicas
    @Query("SELECT * FROM musicas_salvas ORDER BY titulo ASC")
    fun getAll(): Flow<List<MusicaSalva>>

    // READ - música por ID
    @Query("SELECT * FROM musicas_salvas WHERE id = :id")
    suspend fun getById(id: Int): MusicaSalva?

    // UPDATE
    @Update
    suspend fun atualizar(musica: MusicaSalva)

    // DELETE
    @Delete
    suspend fun deletar(musica: MusicaSalva)

    // DELETE por ID
    @Query("DELETE FROM musicas_salvas WHERE id = :id")
    suspend fun deletarPorId(id: Int)
}
