package com.example.musiletra.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.database.AppDatabase
import com.example.musiletra.data.database.Playlist
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = PlaylistRepository(database)

    val playlists: StateFlow<List<Playlist>> = repository.playlists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addPlaylist(nome: String, descricao: String) {
        viewModelScope.launch {
            repository.add(Playlist(nome = nome, descricao = descricao))
        }
    }

    fun editPlaylist(id: Int, nome: String, descricao: String) {
        viewModelScope.launch {
            val existing = repository.get(id)
            if (existing != null) {
                repository.update(existing.copy(nome = nome, descricao = descricao))
            }
        }
    }

    fun deletePlaylist(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    suspend fun getPlaylist(id: Int): Playlist? {
        return repository.get(id)
    }

    fun addMusicaToPlaylist(playlistId: Int, musicaId: Int) {
        viewModelScope.launch {
            repository.addMusicaToPlaylist(playlistId, musicaId)
        }
    }

    fun removeMusicaFromPlaylist(playlistId: Int, musicaId: Int) {
        viewModelScope.launch {
            repository.removeMusicaFromPlaylist(playlistId, musicaId)
        }
    }
}
