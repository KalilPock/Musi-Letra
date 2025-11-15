package com.example.musiletra.data

import com.example.musiletra.model.Song
import kotlinx.coroutines.flow.Flow

class SongRepository(private val songDao: SongDao) {

    // READ - Obter todas as músicas como Flow
    val songs: Flow<List<Song>> = songDao.getAllSongs()

    // CREATE - Adicionar música
    suspend fun add(song: Song) {
        songDao.insert(song)
    }

    // CREATE - Adicionar múltiplas músicas
    suspend fun addAll(songs: List<Song>) {
        songDao.insertAll(songs)
    }

    // READ - Buscar música por ID
    suspend fun get(id: String): Song? {
        return songDao.getSongById(id)
    }

    // READ - Buscar músicas
    fun search(query: String): Flow<List<Song>> {
        return songDao.searchSongs(query)
    }

    // UPDATE - Atualizar música
    suspend fun update(song: Song) {
        songDao.update(song)
    }

    // DELETE - Deletar música
    suspend fun delete(id: String) {
        songDao.deleteById(id)
    }

    // DELETE - Deletar música por objeto
    suspend fun delete(song: Song) {
        songDao.delete(song)
    }
}
