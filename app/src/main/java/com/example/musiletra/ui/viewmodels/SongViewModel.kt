package com.example.musiletra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.AudDSong
import com.example.musiletra.data.RetrofitClient
import com.example.musiletra.data.database.MusicaDao
import com.example.musiletra.data.database.MusicaSalva
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(private val musicaDao: MusicaDao) : ViewModel() {

    private val _currentUserId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val songs: StateFlow<List<MusicaSalva>> = _currentUserId.flatMapLatest { userId ->
        musicaDao.getPlaylistDoUsuario(userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var onlineSearchResults by mutableStateOf<List<AudDSong>>(emptyList())
        private set

    fun setUsuario(usuarioId: Int?) {
        _currentUserId.value = usuarioId
    }

    fun addSong(title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            val novaMusica = MusicaSalva(
                usuarioId = _currentUserId.value,
                titulo = title,
                artista = artist,
                letra = lyrics,
                apiSongId = null
            )
            musicaDao.salvarMusica(novaMusica)
        }
    }

    fun editSong(songId: Int, title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            val musicaAtualizada = MusicaSalva(
                id = songId,
                usuarioId = _currentUserId.value,
                titulo = title,
                artista = artist,
                letra = lyrics,
                apiSongId = null
            )
            musicaDao.atualizarMusica(musicaAtualizada)
        }
    }

    fun deleteSong(musica: MusicaSalva) {
        viewModelScope.launch {
            musicaDao.removerMusica(musica)
        }
    }

    fun searchOnline(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.findByLyrics(query)
                if (response.status == "success") {
                    onlineSearchResults = response.result ?: emptyList()
                } else {
                    val errorMessage = response.error?.message ?: "Unknown error"
                    val errorCode = response.error?.code ?: "N/A"
                    println("AudD API Error ($errorCode): $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onlineSearchResults = emptyList()
            }
        }
    }
}