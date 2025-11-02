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

    // Navigation state
    var currentScreen by mutableStateOf<Screen>(Screen.List)
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
        navigateToList()
    }

    fun editSong(id: String, title: String, artist: String, lyrics: String) {
        val updated = SongRepository.get(id)?.copy(title = title, artist = artist, lyrics = lyrics)
        if (updated != null) SongRepository.update(updated)
        navigateToList()
    }

    fun deleteSong(id: String) {
        SongRepository.delete(id)
        navigateToList()
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

    // --- Navigation ---

    fun openDetail(id: String) {
        selectedSongId = id
        currentScreen = Screen.Detail
    }

    fun navigateToAdd() { currentScreen = Screen.Add }
    fun navigateToEdit(id: String) { selectedSongId = id; currentScreen = Screen.Edit }
    fun navigateToOnlineSearch() { currentScreen = Screen.OnlineSearch; onlineSearchResults = emptyList() }
    fun navigateToList() { currentScreen = Screen.List; selectedSongId = null }
}
