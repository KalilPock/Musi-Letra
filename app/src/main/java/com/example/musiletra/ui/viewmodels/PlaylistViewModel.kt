package com.example.musiletra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.PlaylistWithSongs
import com.example.musiletra.model.Song
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
) : ViewModel() {

    val playlists: StateFlow<List<Playlist>> = playlistRepository.playlists
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val availableSongs: StateFlow<List<Song>> = songRepository.songs
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun createPlaylist(name: String, description: String = "") {
        viewModelScope.launch {
            val playlist = Playlist(name = name, description = description)
            playlistRepository.add(playlist)
        }
    }

    suspend fun getPlaylistWithSongs(id: Int): PlaylistWithSongs? {
        return playlistRepository.getPlaylistWithSongs(id)
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.delete(playlist)
        }
    }

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
