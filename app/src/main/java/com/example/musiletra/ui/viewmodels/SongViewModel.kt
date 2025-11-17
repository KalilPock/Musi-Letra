package com.example.musiletra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.AudDSong
import com.example.musiletra.data.RetrofitClient
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(
    private val repository: SongRepository
) : ViewModel() {

    // State for the user's locally saved songs from the repository's flow
    val songs: StateFlow<List<Song>> = repository.songs.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = emptyList()
    )

    // Private mutable state for the online search results
    private var _onlineSearchResults = MutableStateFlow<List<AudDSong>>(emptyList())


    var onlineSearchResults: StateFlow<List<AudDSong>> = _onlineSearchResults.asStateFlow()

    // --- Local Song Management (CRUD) ---

    fun addSong(title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            repository.add(Song(title = title, artist = artist, lyrics = lyrics))
        }
    }

    fun editSong(id: String, title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            repository.get(id)?.let {
                val updatedSong = it.copy(title = title, artist = artist, lyrics = lyrics)
                repository.update(updatedSong)
            }
        }
    }

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
                    // CORREÇÃO: Emita o novo valor para o MutableStateFlow privado.
                    // Adicione o operador Elvis (?:) para tratar o caso de resultado nulo.
                    _onlineSearchResults.value = response.result ?: emptyList()
                } else {
                    // CORREÇÃO: Limpe os resultados anteriores em caso de erro da API.
                    _onlineSearchResults.value = emptyList()
                    val errorMessage = response.error?.message ?: "Unknown error"
                    val errorCode = response.error?.code ?: "N/A"
                    println("AudD API Error ($errorCode): $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // CORREÇÃO: Limpe os resultados anteriores em caso de exceção na rede.
                _onlineSearchResults.value = emptyList()
            }
        }
    }
}
