package com.example.musiletra.data

import com.example.musiletra.data.database.PlaylistDao
import com.example.musiletra.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistRepository(private val playlistDao: PlaylistDao) {

    // READ - Obter todas as playlists como Flow
    val playlists: Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    // CREATE - Criar nova playlist
    suspend fun add(playlist: Playlist): Long {
        return playlistDao.insert(playlist)
    }

    // READ - Buscar playlist por ID
    suspend fun get(id: Int): Playlist? {
        return playlistDao.getPlaylistById(id)
    }

    // UPDATE - Atualizar playlist
    suspend fun update(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    // DELETE - Deletar playlist
    suspend fun delete(id: Int) {
        playlistDao.deleteById(id)
    }

    // DELETE - Deletar playlist por objeto
    suspend fun delete(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    // Adicionar música à playlist
    suspend fun addSongToPlaylist(playlistId: Int, songId: String) {
        val playlist = get(playlistId) ?: return
        val updatedSongs = playlist.songs.toMutableList().apply {
            if (!contains(songId)) add(songId)
        }
        update(playlist.copy(songs = updatedSongs))
    }

    // Remover música da playlist
    suspend fun removeSongFromPlaylist(playlistId: Int, songId: String) {
        val playlist = get(playlistId) ?: return
        val updatedSongs = playlist.songs.filterNot { it == songId }
        update(playlist.copy(songs = updatedSongs))
    }
}
