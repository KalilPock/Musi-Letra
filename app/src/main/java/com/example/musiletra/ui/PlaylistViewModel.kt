package com.example.musiletra.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.PlaylistWithSongs
import com.example.musiletra.model.Song
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
) : ViewModel() {

    // State for all playlists
    var playlists by mutableStateOf<List<Playlist>>(emptyList())
        private set

    // State for playlists with their songs
    var playlistsWithSongs by mutableStateOf<List<PlaylistWithSongs>>(emptyList())
        private set

    // State for available songs to add to playlists
    var availableSongs by mutableStateOf<List<Song>>(emptyList())
        private set

    init {
        // Load playlists and songs
        viewModelScope.launch {
            playlistRepository.playlists.collect { playlists = it }
        }
        viewModelScope.launch {
            playlistRepository.playlistsWithSongs.collect { playlistsWithSongs = it }
        }
        viewModelScope.launch {
            songRepository.songs.collect { availableSongs = it }
        }
    }

    // --- Playlist CRUD Operations ---

    // CREATE
    fun createPlaylist(name: String, description: String = "") {
        viewModelScope.launch {
            val playlist = Playlist(name = name, description = description)
            playlistRepository.add(playlist)
        }
    }

    // READ
    suspend fun getPlaylist(id: String): Playlist? {
        return playlistRepository.get(id)
    }

    suspend fun getPlaylistWithSongs(id: String): PlaylistWithSongs? {
        return playlistRepository.getWithSongs(id)
    }

    // UPDATE
    fun updatePlaylist(id: String, name: String, description: String) {
        viewModelScope.launch {
            val existing = playlistRepository.get(id)
            if (existing != null) {
                val updated = existing.copy(name = name, description = description)
                playlistRepository.update(updated)
            }
        }
    }

    // DELETE
    fun deletePlaylist(id: String) {
        viewModelScope.launch {
            playlistRepository.delete(id)
        }
    }

    // --- Playlist-Song Management ---

    fun addSongToPlaylist(playlistId: String, songId: String) {
        viewModelScope.launch {
            playlistRepository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun removeSongFromPlaylist(playlistId: String, songId: String) {
        viewModelScope.launch {
            playlistRepository.removeSongFromPlaylist(playlistId, songId)
        }
    }

    fun clearPlaylist(playlistId: String) {
        viewModelScope.launch {
            playlistRepository.clearPlaylist(playlistId)
        }
    }
}
