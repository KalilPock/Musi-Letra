package com.example.musiletra.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.Song
import kotlinx.coroutines.launch

private val static_playlists = listOf<Playlist>(
    Playlist(
        id = 1,
        name = "Rock Classics",
        songs = listOf("s1", "s2"),
        description = "Musicas de rock clássicas"
    ), Playlist(
        id = 2,
        name = "Lofi Beats to Study To",
        songs = listOf("s3"),
        description = "Musicas para relaxar e estudar"
    ), Playlist(
        id = 3,
        name = "80s Pop Hits",
        songs = emptyList(),
        description = "Musicas populares da década de 80"
    ), Playlist(
        id = 4,
        name = "Acoustic Mornings",
        songs = listOf("s4", "s5"),
        description = "Musicas para acordar cedo"
    )
)

class PlaylistsViewModel : ViewModel() {
    //  private var playlists = mutableListOf<Playlist>()
    //  - mutableListOf não altera automaticamente o state da playlistsScreen
    var playlists by mutableStateOf(emptyList<Playlist>())
        private set

    // Função pra carregar as músicas
    private fun loadPlaylists() {
        viewModelScope.launch {
            playlists = static_playlists
        }
    }

    init {
        loadPlaylists()
    }

    fun addPlaylist(
        name: String, description: String?, songs: List<String>,
    ) {
        try {
            val nextId = (playlists.maxOfOrNull { it.id } ?: 0) + 1

            val newPlaylist = Playlist(
                id = nextId,
                name = name,
                description = description,
                songs = songs,
            )

            playlists = playlists.plus(newPlaylist)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPlaylist(idPlaylist: Int): Playlist? {
        return playlists.find { it.id == idPlaylist }
    }

    fun updatePlaylist(
        playlistId: Int, newName: String?,
        newDescription: String?,
        newSongs: List<String>?,
    ) {
        try {
            val updatedPlaylist = playlists.map { playlist ->
                if (playlist.id == playlistId) {
                    playlist.copy(
                        name = newName ?: playlist.name,
                        description = newDescription ?: playlist.description,
                        songs = newSongs ?: playlist.songs,
                    )
                } else {
                    playlist
                }
            }

            playlists = updatedPlaylist
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun deletePlaylist(playlistId: Int) {
        try {
            playlists = playlists.filter { it.id != playlistId }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}