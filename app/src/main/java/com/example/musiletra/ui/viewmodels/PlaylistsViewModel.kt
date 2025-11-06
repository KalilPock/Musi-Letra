package com.example.musiletra.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.Song
import kotlinx.coroutines.launch

private val static_playlists = listOf<Playlist>(
    Playlist(
        id = 1, name = "Rock Classics",
        songs = listOf(
            Song(
                id = "1",
                title = "Bohemian Rhapsody",
                artist = "Queen",
                lyrics = "Lyrics...",
            ),
            Song(
                id = "2",
                title = "Stairway to Heaven",
                artist = "Led Zeppelin",
                lyrics = "Lyrics...",
            ),
        ),
    ), Playlist(
        id = 2, name = "Lofi Beats to Study To",
        songs = listOf(
            Song(
                id = "3",
                title = "Affection",
                artist = "Jinsang",
                lyrics = "Lyrics...",
            ),
            Song(
                id = "4",
                title = "Sunday Morning",
                artist = "Trell Daniels",
                lyrics = "Lyrics...",
            ),
        ),
    ), Playlist(
        id = 3, name = "80s Pop Hits",
        songs = listOf(
            Song(
                id = "5",
                title = "Fast Car",
                artist = "Tracy Chapman",
                lyrics = "Lyrics...",
            ),
        ),
    ), Playlist(
        id = 4, name = "Acoustic Mornings", songs = listOf(
            Song(
                id = "6",
                title = "Fast Car",
                artist = "Tracy Chapman",
                lyrics = "Lyrics...",
            ),
        )
    )
)

class PlaylistsViewModel : ViewModel() {
    //  private var _playlists = mutableListOf<Playlist>()
//  - mutableListOf vai fazer com que não altere o state da playlistsScreen
    private var _playlists = mutableStateOf(emptyList<Playlist>())
    val playlist: State<List<Playlist>> get() = _playlists

    fun startLoadingPlaylists() {
        viewModelScope.launch {
            loadPlaylists()
        }
    }

    private suspend fun loadPlaylists() {

        _playlists.value = static_playlists

    }
}