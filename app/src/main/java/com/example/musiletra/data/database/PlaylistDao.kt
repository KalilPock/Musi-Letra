package com.example.musiletra.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    // CREATE
    @Insert
    suspend fun inserir(playlist: Playlist): Long

    // READ - todas as playlists
    @Query("SELECT * FROM playlists ORDER BY nome ASC")
    fun getAll(): Flow<List<Playlist>>

    // READ - playlist por ID
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getById(id: Int): Playlist?

    // UPDATE
    @Update
    suspend fun atualizar(playlist: Playlist)

    // DELETE
    @Delete
    suspend fun deletar(playlist: Playlist)

    // DELETE por ID
    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletarPorId(id: Int)
}
