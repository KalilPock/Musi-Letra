package com.example.musiletra.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.AudDSong
import com.example.musiletra.data.RetrofitClient
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.Song
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {
    // State for the user's locally saved songs
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set

    // State for the online search results
    var onlineSearchResults by mutableStateOf<List<AudDSong>>(emptyList())
        private set

    var selectedSongId by mutableStateOf<String?>(null)
        private set

    init {
        // Load the locally saved songs
        viewModelScope.launch {
            SongRepository.songs.collect { songs = it }
        }
    }

    // --- Local Song Management ---

    fun addSong(title: String, artist: String, lyrics: String) {
        SongRepository.add(Song(title = title, artist = artist, lyrics = lyrics))
        // Optional: navigate back to the main list after adding
    }

    fun editSong(id: String, title: String, artist: String, lyrics: String) {
        val updated = SongRepository.get(id)?.copy(title = title, artist = artist, lyrics = lyrics)
        if (updated != null) SongRepository.update(updated)
    }

    fun deleteSong(id: String) {
        SongRepository.delete(id)
    }

    // --- Online Search ---

    fun searchOnline(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.findByLyrics(query)
                if (response.status == "success") {
                    onlineSearchResults = response.result ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle network errors, maybe show a toast or a message
                onlineSearchResults = emptyList()
            }
        }
    }
}
