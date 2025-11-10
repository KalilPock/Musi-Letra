package com.example.musiletra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.AudDSong
import com.example.musiletra.data.RetrofitClient
import com.example.musiletra.model.Song
import kotlinx.coroutines.launch
import java.util.UUID

val static_songs = listOf(
    // Rock Classics
    Song(
        id = "s1",
        title = "Bohemian Rhapsody",
        artist = "Queen",
        lyrics = "Is this the real life? Is this just fantasy?..."
    ), Song(
        id = "s2",
        title = "Stairway to Heaven",
        artist = "Led Zeppelin",
        lyrics = "There's a lady who's sure all that glitters is gold..."
    ),
    // Lofi Beats
    Song(
        id = "s3", title = "Affection", artist = "Jinsang", lyrics = "[Instrumental]"
    ),
    // Acoustic Mornings
    Song(
        id = "s4",
        title = "Fast Car",
        artist = "Tracy Chapman",
        lyrics = "You got a fast car, I want a ticket to anywhere..."
    ), Song(
        id = "s5",
        title = "Wonderwall",
        artist = "Oasis",
        lyrics = "Today is gonna be the day that they're gonna throw it back to you..."
    )
)

class SongViewModel : ViewModel() {
    // State for the user's locally saved songs
    var songs by mutableStateOf(emptyList<Song>())
        private set

    // State for the online search results
    var onlineSearchResults by mutableStateOf<List<AudDSong>>(emptyList())
        private set

    init {
        loadSongs()
    }

    private fun loadSongs() {
        // Load the locally saved songs
        viewModelScope.launch {
            songs = static_songs
            // SongRepository.songs.collect { songs = it }
        }

    }

    // --- Local Song Management ---

    fun addSong(title: String, artist: String, lyrics: String) {
        val nextId = UUID.randomUUID().toString()

        val newSong = Song(
            id = nextId,
            title = title,
            artist = artist,
            lyrics = lyrics,
        )

        songs = songs.plus(newSong)
//        SongRepository.add(Song(title = title, artist = artist, lyrics = lyrics))
        // Optional: navigate back to the main list after adding
    }

    fun editSong(songId: String, title: String, artist: String, lyrics: String) {

        val updatedSongs = songs.map { song ->
            if (song.id == songId) {
                song.copy(
                    title = title,
                    artist = artist,
                    lyrics = lyrics,
                )

            } else {
                song
            }
        }

        songs = updatedSongs

//        val updated = SongRepository.get(id)?.copy(title = title, artist = artist, lyrics = lyrics)
//        if (updated != null) SongRepository.update(updated)
    }

    fun deleteSong(songId: String) {
        try {
            songs = songs.filter { it.id != songId }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Online Search ---

    fun searchOnline(query: String) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitClient.apiService.findByLyrics(query)
//                if (response.status == "success") {
//                    onlineSearchResults = response.result ?: emptyList()
//                }
//            } catch (e: Exception) {
//                // Handle network errors, maybe show a toast or a message
//                onlineSearchResults = emptyList()
//            }
//        }
    }
}