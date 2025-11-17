package com.example.musiletra.data

import com.example.musiletra.data.database.AppDatabase
import com.example.musiletra.data.database.MusicaSalva
import kotlinx.coroutines.flow.Flow

class SongRepository(private val database: AppDatabase) {
    private val musicaDao = database.musicaDao()

    val songs: Flow<List<MusicaSalva>> = musicaDao.getAll()

    suspend fun add(musica: MusicaSalva) {
        musicaDao.inserir(musica)
    }

    suspend fun update(musica: MusicaSalva) {
        musicaDao.atualizar(musica)
    }

    suspend fun delete(id: Int) {
        musicaDao.deletarPorId(id)
    }

    suspend fun get(id: Int): MusicaSalva? {
        return musicaDao.getById(id)
    }
}
