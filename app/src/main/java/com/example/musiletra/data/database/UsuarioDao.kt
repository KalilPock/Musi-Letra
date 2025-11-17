package com.example.musiletra.data.database

import androidx.room.*

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserir(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email AND senhaHash = :senhaHash LIMIT 1")
    suspend fun login(email: String, senhaHash: String): Usuario?

    @Update
    suspend fun atualizar(usuario: Usuario)

    @Delete
    suspend fun deletar(usuario: Usuario)
}
