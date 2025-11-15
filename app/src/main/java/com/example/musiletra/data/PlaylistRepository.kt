package com.example.musiletra.data

import com.example.musiletra.model.Playlist
import com.example.musiletra.model.PlaylistSongCrossRef
import com.example.musiletra.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

class PlaylistRepository(private val playlistDao: PlaylistDao) {

    // READ - Obter todas as playlists como Flow
    val playlists: Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    // READ - Obter todas as playlists com suas músicas
    val playlistsWithSongs: Flow<List<PlaylistWithSongs>> = playlistDao.getAllPlaylistsWithSongs()

    // CREATE - Criar nova playlist
    suspend fun add(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    // READ - Buscar playlist por ID
    suspend fun get(id: String): Playlist? {
        return playlistDao.getPlaylistById(id)
    }

    // READ - Buscar playlist com suas músicas
    suspend fun getWithSongs(id: String): PlaylistWithSongs? {
        return playlistDao.getPlaylistWithSongs(id)
    }

    // UPDATE - Atualizar playlist
    suspend fun update(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    // DELETE - Deletar playlist
    suspend fun delete(id: String) {
        playlistDao.deleteById(id)
    }

    // DELETE - Deletar playlist por objeto
    suspend fun delete(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    // Adicionar música à playlist
    suspend fun addSongToPlaylist(playlistId: String, songId: String) {
        playlistDao.addSongToPlaylist(PlaylistSongCrossRef(playlistId, songId))
    }

    // Remover música da playlist
    suspend fun removeSongFromPlaylist(playlistId: String, songId: String) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }

    // Remover todas as músicas de uma playlist
    suspend fun clearPlaylist(playlistId: String) {
        playlistDao.removeAllSongsFromPlaylist(playlistId)
    }
}
