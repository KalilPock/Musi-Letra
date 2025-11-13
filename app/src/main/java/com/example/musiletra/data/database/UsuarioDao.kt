package com.example.musiletra.data.database

import androidx.room.*

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserirUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioPorEmail(email: String): Usuario?
    
    // (Adicione @Update e @Delete se precisar para o CRUD completo)
}