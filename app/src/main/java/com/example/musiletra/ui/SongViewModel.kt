package com.example.musiletra.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.AudDSong
import com.example.musiletra.data.RetrofitClient
import com.example.musiletra.data.SongRepository
import com.example.musiletra.data.database.AppDatabase
import com.example.musiletra.data.database.MusicaSalva
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = SongRepository(database)

    // State for the user's locally saved songs
    val songs: StateFlow<List<MusicaSalva>> = repository.songs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // State for the online search results
    var onlineSearchResults by mutableStateOf<List<AudDSong>>(emptyList())
        private set

    // --- Local Song Management ---

    fun addSong(title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            repository.add(MusicaSalva(titulo = title, artista = artist, letra = lyrics))
        }
    }

    fun editSong(id: Int, title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            val existing = repository.get(id)
            if (existing != null) {
                repository.update(existing.copy(titulo = title, artista = artist, letra = lyrics))
            }
        }
    }

    fun deleteSong(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    suspend fun getSong(id: Int): MusicaSalva? {
        return repository.get(id)
    }

    // --- Online Search ---

    fun searchOnline(query: String) {
        viewModelScope.launch {
            try {
                println("🔍 Buscando letras online para: $query")
                val response = RetrofitClient.apiService.findByLyrics(query)
                println("✅ Resposta da API - Status: ${response.status}")
                println("✅ Número de resultados: ${response.result?.size ?: 0}")

                if (response.status == "success") {
                    onlineSearchResults = response.result ?: emptyList()
                    println("✅ Resultados atualizados: ${onlineSearchResults.size} músicas encontradas")
                } else {
                    println("⚠️ Status da API não é success: ${response.status}")
                    onlineSearchResults = emptyList()
                }
            } catch (e: Exception) {
                println("❌ Erro ao buscar letras: ${e.message}")
                e.printStackTrace()
                onlineSearchResults = emptyList()
            }
        }
    }
}
