package com.example.musiletra.data

import com.example.musiletra.data.database.PlaylistDao
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.PlaylistSongCrossRef
import com.example.musiletra.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

class PlaylistRepository(private val playlistDao: PlaylistDao) {

    val playlists: Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    val playlistsWithSongs: Flow<List<PlaylistWithSongs>> = playlistDao.getAllPlaylistsWithSongs()

    suspend fun add(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    suspend fun getPlaylistWithSongs(id: Int): PlaylistWithSongs? {
        return playlistDao.getPlaylistWithSongs(id)
    }

    suspend fun update(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    suspend fun delete(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    suspend fun addSongToPlaylist(playlistId: Int, songId: String) {
        val crossRef = PlaylistSongCrossRef(playlistId = playlistId, songId = songId)
        playlistDao.addSongToPlaylist(crossRef)
    }

    suspend fun removeSongFromPlaylist(playlistId: Int, songId: String) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }
}
