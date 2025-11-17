package com.example.musiletra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.Song
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
) : ViewModel() {

    // State for all playlists
    var playlists by mutableStateOf<List<Playlist>>(emptyList())
        private set

    // State for playlists with their songs populated
    var playlistsWithSongs by mutableStateOf<Map<Int, List<Song>>>(emptyMap())
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
            songRepository.songs.collect { availableSongs = it }
        }

        // Combine playlists and songs to populate playlistsWithSongs
        viewModelScope.launch {
            combine(
                playlistRepository.playlists,
                songRepository.songs
            ) { playlistsList, songsList ->
                playlistsList.associateWith { playlist ->
                    playlist.songs.mapNotNull { songId ->
                        songsList.firstOrNull { it.id == songId }
                    }
                }
            }.collect { result ->
                playlistsWithSongs = result.mapKeys { it.key.id }
            }
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
    suspend fun getPlaylist(id: Int): Playlist? {
        return playlistRepository.get(id)
    }

    fun getPlaylistSongs(playlistId: Int): List<Song> {
        return playlistsWithSongs[playlistId] ?: emptyList()
    }

    // UPDATE
    fun updatePlaylist(id: Int, name: String, description: String) {
        viewModelScope.launch {
            val existing = playlistRepository.get(id)
            if (existing != null) {
                val updated = existing.copy(name = name, description = description)
                playlistRepository.update(updated)
            }
        }
    }

    // DELETE
    fun deletePlaylist(id: Int) {
        viewModelScope.launch {
            playlistRepository.delete(id)
        }
    }

    // --- Playlist-Song Management ---

    fun addSongToPlaylist(playlistId: Int, songId: String) {
        viewModelScope.launch {
            playlistRepository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun removeSongFromPlaylist(playlistId: Int, songId: String) {
        viewModelScope.launch {
            playlistRepository.removeSongFromPlaylist(playlistId, songId)
        }
    }
}
