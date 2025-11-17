package com.example.musiletra.data

import com.example.musiletra.data.database.AppDatabase
import com.example.musiletra.data.database.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistRepository(private val database: AppDatabase) {
    private val playlistDao = database.playlistDao()

    val playlists: Flow<List<Playlist>> = playlistDao.getAll()

    suspend fun add(playlist: Playlist) {
        playlistDao.inserir(playlist)
    }

    suspend fun update(playlist: Playlist) {
        playlistDao.atualizar(playlist)
    }

    suspend fun delete(id: Int) {
        playlistDao.deletarPorId(id)
    }

    suspend fun get(id: Int): Playlist? {
        return playlistDao.getById(id)
    }

    // Adicionar música a uma playlist
    suspend fun addMusicaToPlaylist(playlistId: Int, musicaId: Int) {
        val playlist = playlistDao.getById(playlistId) ?: return
        val currentIds = if (playlist.musicaIds.isEmpty()) {
            emptyList()
        } else {
            playlist.musicaIds.split(",").map { it.toIntOrNull() }.filterNotNull()
        }

        if (!currentIds.contains(musicaId)) {
            val newIds = (currentIds + musicaId).joinToString(",")
            playlistDao.atualizar(playlist.copy(musicaIds = newIds))
        }
    }

    // Remover música de uma playlist
    suspend fun removeMusicaFromPlaylist(playlistId: Int, musicaId: Int) {
        val playlist = playlistDao.getById(playlistId) ?: return
        val currentIds = if (playlist.musicaIds.isEmpty()) {
            emptyList()
        } else {
            playlist.musicaIds.split(",").map { it.toIntOrNull() }.filterNotNull()
        }

        val newIds = currentIds.filter { it != musicaId }.joinToString(",")
        playlistDao.atualizar(playlist.copy(musicaIds = newIds))
    }
}
