package com.example.musiletra.ui.viewmodels

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

class SongViewModel(
    private val repository: SongRepository
) : ViewModel() {
    // State for the user's locally saved songs
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set

    // State for the online search results
    var onlineSearchResults by mutableStateOf<List<AudDSong>>(emptyList())
        private set

    init {
        // Load the locally saved songs from Room database
        viewModelScope.launch {
            repository.songs.collect { songs = it }
        }
    }

    // --- Local Song Management (CRUD) ---

    // CREATE
    fun addSong(title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            repository.add(Song(title = title, artist = artist, lyrics = lyrics))
        }
    }

    fun addSong(song: Song) {
        viewModelScope.launch {
            repository.add(song)
        }
    }

    // READ
    suspend fun getSong(id: String): Song? {
        return repository.get(id)
    }

    // UPDATE
    fun editSong(id: String, title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            val existing = repository.get(id)
            if (existing != null) {
                val updated = existing.copy(title = title, artist = artist, lyrics = lyrics)
                repository.update(updated)
            }
        }
    }

    // DELETE
    fun deleteSong(id: String) {
        viewModelScope.launch {
            repository.delete(id)
        }
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
                onlineSearchResults = emptyList()
            }
        }
    }
}
